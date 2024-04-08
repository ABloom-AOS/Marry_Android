package com.abloom.mery.data.repository

import com.abloom.domain.question.model.Question
import com.abloom.domain.question.repository.QuestionRepository
import com.abloom.domain.user.repository.UserRepository
import com.abloom.mery.data.database.RecommendationQuestionDao
import com.abloom.mery.data.database.RecommendationQuestionEntity
import com.abloom.mery.data.firebase.question.QuestionDocument
import com.abloom.mery.data.firebase.question.QuestionFirebaseDataSource
import com.abloom.mery.data.firebase.question.asExternal
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class DefaultQuestionRepository @Inject constructor(
    private val userRepository: UserRepository,
    private val roomDataSource: RecommendationQuestionDao,
    private val questionFirebaseDataSource: QuestionFirebaseDataSource,
) : QuestionRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getTodayRecommendationQuestionFlow(): Flow<Question?> = userRepository.getLoginUserFlow()
        .flatMapLatest { loginUser ->
            val loginUserId = loginUser?.id ?: RecommendationQuestionEntity.NOT_LOGIN_ID
            roomDataSource.getRecommendationQuestionIdFlow(loginUserId, LocalDate.now())
                .map { questionId ->
                    if (questionId == null) return@map null
                    val questionDocument = questionFirebaseDataSource.getQuestion(questionId)
                    questionDocument?.asExternal()
                }
        }

    override suspend fun setTodayRecommendationQuestion(questionId: Long) {
        val userId = userRepository.loginUserId
        val entity = RecommendationQuestionEntity.create(
            userId = userId,
            date = LocalDate.now(),
            questionId = questionId
        )
        roomDataSource.setRecommendationQuestion(entity)
    }

    override suspend fun getEssentialQuestionIds(): List<Long> = questionFirebaseDataSource
        .getEssentialQuestionIds()

    override fun getQuestionsFlow(): Flow<List<Question>> = questionFirebaseDataSource
        .getQuestionsFlow()
        .map(List<QuestionDocument>::asExternal)

    override suspend fun getQuestions(): List<Question> = questionFirebaseDataSource.getQuestions()
        .map(QuestionDocument::asExternal)

    override fun getQuestionFlow(id: Long): Flow<Question> =
        questionFirebaseDataSource.getQuestionFlow(id)
            .filterNotNull()
            .map(QuestionDocument::asExternal)
}
