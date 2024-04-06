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
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class DefaultQuestionRepository @Inject constructor(
    private val userRepository: UserRepository,
    private val roomDataSource: RecommendationQuestionDao,
    private val firebaseDataSource: QuestionFirebaseDataSource
) : QuestionRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getTodayRecommendationQuestion(): Flow<Question> = userRepository.getLoginUser()
        .flatMapLatest outer@{ loginUser ->
            val loginUserId = loginUser?.id ?: RecommendationQuestionEntity.NOT_LOGIN_ID
            roomDataSource.getRecommendationQuestionId(loginUserId, LocalDate.now())
                .flatMapLatest { questionId ->
                    if (questionId == null) return@flatMapLatest flow { }
                    getQuestion(questionId)
                }
        }

    override suspend fun setTodayRecommendationQuestion(question: Question) {
        val userId = userRepository.getLoginUser().lastOrNull()?.id
        val entity = RecommendationQuestionEntity.create(
            userId = userId,
            date = LocalDate.now(),
            questionId = question.id
        )
        roomDataSource.setRecommendationQuestion(entity)
    }

    override fun getEssentialQuestions(): Flow<List<Question>> = firebaseDataSource
        .getEssentialQuestions()
        .map(List<QuestionDocument>::asExternal)

    override fun getQuestions(): Flow<List<Question>> = firebaseDataSource
        .getQuestions()
        .map(List<QuestionDocument>::asExternal)

    override fun getQuestion(id: Long): Flow<Question> = firebaseDataSource.getQuestion(id)
        .map(QuestionDocument::asExternal)
}
