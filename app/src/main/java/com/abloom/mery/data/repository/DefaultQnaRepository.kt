package com.abloom.mery.data.repository

import com.abloom.domain.qna.model.Answer
import com.abloom.domain.qna.model.FinishedQna
import com.abloom.domain.qna.model.Qna
import com.abloom.domain.qna.model.Response
import com.abloom.domain.qna.model.UnfinishedResponseQna
import com.abloom.domain.qna.repository.ProspectiveCoupleQnaRepository
import com.abloom.domain.question.model.Question
import com.abloom.domain.question.repository.QuestionRepository
import com.abloom.domain.user.model.User
import com.abloom.domain.user.repository.UserRepository
import com.abloom.mery.data.di.ApplicationScope
import com.abloom.mery.data.firebase.qna.QnaDocument1
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
import kotlinx.coroutines.flow.flowOf
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
    override fun getQnasFlow(): Flow<List<Qna>> = combine(
        userRepository.getLoginUserFlow(),
        userRepository.getFianceFlow(),
        questionRepository.getQuestionsFlow()
    ) { loginUser, fiance, questions ->
        if (loginUser == null) return@combine flowOf(emptyList())
        if (fiance == null) return@combine getQnasFlow(loginUser, questions)
        getQnasFlow(loginUser, fiance, questions)
    }.flatMapLatest { it }
        .map { it.sorted() }

    private fun getQnasFlow(
        loginUser: User,
        questions: List<Question>
    ): Flow<List<Qna>> = firebaseDataSource.getQnaDocumentsFlow(loginUser.id)
        .map { qnaDocuments ->
            qnaDocuments.map { qnaDocument ->
                Qna.create(
                    question = questions.first { it.id == qnaDocument.q_id },
                    createdAt = qnaDocument.date.toLocalDateTime(),
                    loginUser = loginUser,
                    loginUserAnswer = Answer(qnaDocument.answer_content)
                )
            }
        }

    private fun getQnasFlow(
        loginUser: User,
        fiance: User,
        questions: List<Question>
    ): Flow<List<Qna>> = combine(
        firebaseDataSource.getQnaDocumentsFlow(loginUser.id),
        firebaseDataSource.getQnaDocumentsFlow(fiance.id)
    ) { loginUserQnaDocuments, fianceQnaDocuments ->
        (loginUserQnaDocuments + fianceQnaDocuments).groupBy { it.q_id }
            .values.map { qnaDocuments ->
                val loginUserQnaDocument = qnaDocuments.firstOrNull { it.user_id == loginUser.id }
                val fianceQnaDocument = qnaDocuments.firstOrNull { it.user_id == fiance.id }

                Qna.create(
                    question = questions.first { it.id == qnaDocuments[0].q_id },
                    createdAt = minOf(
                        loginUserQnaDocument?.date?.toLocalDateTime() ?: LocalDateTime.now(),
                        fianceQnaDocument?.date?.toLocalDateTime() ?: LocalDateTime.now()
                    ),
                    loginUser = loginUser,
                    fiance = fiance,
                    loginUserAnswer = loginUserQnaDocument?.answer_content?.let { Answer(it) },
                    fianceAnswer = fianceQnaDocument?.answer_content?.let { Answer(it) },
                    loginUserResponse = loginUserQnaDocument?.reaction?.asResponse(),
                    fianceResponse = fianceQnaDocument?.reaction?.asResponse()
                )
            }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getQnaFlow(questionId: Long): Flow<Qna> = combine(
        userRepository.getLoginUserFlow(),
        userRepository.getFianceFlow(),
        questionRepository.getQuestionFlow(questionId)
    ) { loginUser, fiance, question ->
        if (loginUser == null) return@combine flow { }
        if (fiance == null) return@combine getQnaFlow(loginUser, question)
        getQnaFlow(loginUser = loginUser, fiance = fiance, question = question)
    }.flatMapLatest { it }

    private fun getQnaFlow(loginUser: User, question: Question): Flow<Qna> =
        firebaseDataSource.getQnaDocumentFlow(loginUser.id, question.id)
            .map { qnaDocument ->
                if (qnaDocument == null) return@map null
                Qna.create(
                    question = question,
                    createdAt = qnaDocument.date.toLocalDateTime(),
                    loginUser = loginUser,
                    loginUserAnswer = Answer(qnaDocument.answer_content)
                )
            }
            .filterNotNull()

    private fun getQnaFlow(
        loginUser: User,
        fiance: User,
        question: Question
    ): Flow<Qna> = combine(
        firebaseDataSource.getQnaDocumentFlow(loginUser.id, question.id),
        firebaseDataSource.getQnaDocumentFlow(fiance.id, question.id)
    ) { loginUserQnaDocument, fianceQnaDocument ->
        when {
            loginUserQnaDocument != null && fianceQnaDocument != null -> Qna.create(
                question = question,
                createdAt = minOf(
                    loginUserQnaDocument.date.toLocalDateTime(),
                    fianceQnaDocument.date.toLocalDateTime()
                ),
                loginUser = loginUser,
                fiance = fiance,
                loginUserAnswer = Answer(loginUserQnaDocument.answer_content),
                fianceAnswer = Answer(fianceQnaDocument.answer_content),
                loginUserResponse = loginUserQnaDocument.reaction?.asResponse(),
                fianceResponse = fianceQnaDocument.reaction?.asResponse()
            )

            loginUserQnaDocument != null -> Qna.create(
                question = question,
                createdAt = loginUserQnaDocument.date.toLocalDateTime(),
                loginUser = loginUser,
                fiance = fiance,
                loginUserAnswer = Answer(loginUserQnaDocument.answer_content)
            )

            fianceQnaDocument != null -> Qna.create(
                question = question,
                createdAt = fianceQnaDocument.date.toLocalDateTime(),
                loginUser = loginUser,
                fiance = fiance,
                fianceAnswer = Answer(fianceQnaDocument.answer_content)
            )

            else -> null
        }
    }.filterNotNull()

    override suspend fun answerQna(questionId: Long, answer: Answer) = externalScope.launch {
        val loginUserId = userRepository.loginUserId ?: return@launch
        val qnaDocument = QnaDocument1(
            userId = loginUserId,
            questionId = questionId,
            date = LocalDateTime.now(),
            answer = answer.value
        )
        firebaseDataSource.createQnaDocument(qnaDocument)
    }.join()

    override suspend fun respondToQna(
        qna: UnfinishedResponseQna,
        response: Response
    ) = externalScope.launch {
        val loginUserId = userRepository.loginUserId ?: return@launch
        firebaseDataSource.updateReaction(
            loginUserId = loginUserId,
            fianceId = qna.fiance.id,
            questionId = qna.question.id,
            reaction = response.asReaction()
        )
    }.join()

    override suspend fun changeResponse(
        qna: FinishedQna,
        response: Response
    ) = externalScope.launch {
        val loginUserId = userRepository.loginUserId ?: return@launch
        firebaseDataSource.updateReaction(
            loginUserId = loginUserId,
            fianceId = qna.fiance.id,
            questionId = qna.question.id,
            reaction = response.asReaction()
        )
    }.join()
}
