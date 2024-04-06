package com.abloom.mery.data.repository

import com.abloom.domain.qna.model.Answer
import com.abloom.domain.qna.model.Qna
import com.abloom.domain.qna.model.Response
import com.abloom.domain.qna.repository.ProspectiveCoupleQnaRepository
import com.abloom.domain.question.model.Question
import com.abloom.domain.question.repository.QuestionRepository
import com.abloom.domain.user.model.User
import com.abloom.domain.user.repository.UserRepository
import com.abloom.mery.data.di.ApplicationScope
import com.abloom.mery.data.firebase.qna.QnaDocument
import com.abloom.mery.data.firebase.qna.QnaFirebaseDataSource
import com.abloom.mery.data.firebase.qna.asReaction
import com.abloom.mery.data.firebase.qna.asResponse
import com.abloom.mery.data.firebase.toLocalDateTime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

class DefaultQnaRepository @Inject constructor(
    @ApplicationScope private val externalScope: CoroutineScope,
    private val userRepository: UserRepository,
    private val questionRepository: QuestionRepository,
    private val firebaseDataSource: QnaFirebaseDataSource
) : ProspectiveCoupleQnaRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getQnas(): Flow<List<Qna>> = combine(
        userRepository.getLoginUser(),
        userRepository.getFiance(),
        questionRepository.getQuestions()
    ) { loginUser, fiance, questions ->
        if (loginUser == null) return@combine flow { emit(emptyList()) }
        if (fiance == null) return@combine getQnasFlow(loginUser, questions)
        getQnasFlow(loginUser, fiance, questions)
    }.flatMapLatest { it }

    private fun getQnasFlow(
        loginUser: User,
        questions: List<Question>
    ): Flow<List<Qna>> = firebaseDataSource.getQnaDocuments(loginUser.id)
        .map { qnaDocuments ->
            qnaDocuments.map { qnaDocument ->
                Qna.create(
                    question = questions.first { it.id == qnaDocument.questionId },
                    createdAt = qnaDocument.date.toLocalDateTime(),
                    loginUser = loginUser,
                    loginUserAnswer = Answer(qnaDocument.answer)
                )
            }
        }

    private fun getQnasFlow(
        loginUser: User,
        fiance: User,
        questions: List<Question>
    ): Flow<List<Qna>> = combine(
        firebaseDataSource.getQnaDocuments(loginUser.id),
        firebaseDataSource.getQnaDocuments(fiance.id)
    ) { loginUserQnaDocuments, fianceQnaDocuments ->
        (loginUserQnaDocuments + fianceQnaDocuments).groupBy { it.questionId }
            .values.map { qnaDocuments ->
                if (qnaDocuments.size == 1) {
                    val document = qnaDocuments.first()
                    val isLoginUserDocument = document.userId == loginUser.id
                    return@map Qna.create(
                        question = questions.first { it.id == document.questionId },
                        createdAt = document.date.toLocalDateTime(),
                        loginUser = loginUser,
                        fiance = fiance,
                        loginUserAnswer = if (isLoginUserDocument) Answer(document.answer) else null,
                        fianceAnswer = if (!isLoginUserDocument) Answer(document.answer) else null
                    )
                }
                val loginUserQnaDocument = qnaDocuments.first { it.userId == loginUser.id }
                val fianceQnaDocument = qnaDocuments.first { it.userId == fiance.id }
                Qna.create(
                    question = questions.first { it.id == loginUserQnaDocument.questionId },
                    createdAt = minOf(
                        loginUserQnaDocument.date,
                        fianceQnaDocument.date
                    ).toLocalDateTime(),
                    loginUser = loginUser,
                    fiance = fiance,
                    loginUserAnswer = Answer(loginUserQnaDocument.answer),
                    fianceAnswer = Answer(fianceQnaDocument.answer),
                    loginUserResponse = loginUserQnaDocument.reaction?.asResponse(),
                    fianceResponse = fianceQnaDocument.reaction?.asResponse()
                )
            }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getQna(questionId: Long): Flow<Qna> = combine(
        userRepository.getLoginUser(),
        userRepository.getFiance(),
        questionRepository.getQuestion(questionId)
    ) { loginUser, fiance, question ->
        if (loginUser == null) return@combine flow { }
        if (fiance == null) return@combine getQnaFlow(loginUser, question)
        getQnaFlow(loginUser = loginUser, fiance = fiance, question = question)
    }.flatMapLatest { it }

    private fun getQnaFlow(loginUser: User, question: Question): Flow<Qna> =
        firebaseDataSource.getQnaDocument(loginUser.id, question.id)
            .map { qnaDocument ->
                if (qnaDocument == null) return@map null
                Qna.create(
                    question = question,
                    createdAt = qnaDocument.date.toLocalDateTime(),
                    loginUser = loginUser,
                    loginUserAnswer = Answer(qnaDocument.answer)
                )
            }
            .filterNotNull()

    private fun getQnaFlow(
        loginUser: User,
        fiance: User,
        question: Question
    ): Flow<Qna> = combine(
        firebaseDataSource.getQnaDocument(loginUser.id, question.id),
        firebaseDataSource.getQnaDocument(fiance.id, question.id)
    ) { loginUserQnaDocument, fianceQnaDocument ->
        when {
            loginUserQnaDocument != null && fianceQnaDocument != null -> Qna.create(
                question = question,
                createdAt = minOf(
                    loginUserQnaDocument.date,
                    fianceQnaDocument.date
                ).toLocalDateTime(),
                loginUser = loginUser,
                fiance = fiance,
                loginUserAnswer = Answer(loginUserQnaDocument.answer),
                fianceAnswer = Answer(fianceQnaDocument.answer),
                loginUserResponse = loginUserQnaDocument.reaction?.asResponse(),
                fianceResponse = fianceQnaDocument.reaction?.asResponse()
            )

            loginUserQnaDocument != null -> Qna.create(
                question = question,
                createdAt = loginUserQnaDocument.date.toLocalDateTime(),
                loginUser = loginUser,
                fiance = fiance,
                loginUserAnswer = Answer(loginUserQnaDocument.answer)
            )

            fianceQnaDocument != null -> Qna.create(
                question = question,
                createdAt = fianceQnaDocument.date.toLocalDateTime(),
                loginUser = loginUser,
                fiance = fiance,
                fianceAnswer = Answer(fianceQnaDocument.answer)
            )

            else -> null
        }
    }.filterNotNull()

    override suspend fun answerQna(questionId: Long, answer: Answer) = externalScope.launch {
        val loginUserId = userRepository.loginUserId ?: return@launch
        val qnaDocument = QnaDocument.create(
            userId = loginUserId,
            questionId = questionId,
            date = LocalDateTime.now(),
            answer = answer.value
        )
        firebaseDataSource.createQnaDocument(qnaDocument)
    }.join()

    override suspend fun respondToQna(questionId: Long, response: Response) = externalScope.launch {
        val loginUserId = userRepository.loginUserId ?: return@launch
        firebaseDataSource.updateReaction(loginUserId, questionId, response.asReaction())
    }.join()
}
