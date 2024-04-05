package com.abloom.mery.data.repository

import com.abloom.domain.question.model.Question
import com.abloom.domain.question.repository.QuestionRepository
import com.abloom.mery.data.firebase.QuestionDocument
import com.abloom.mery.data.firebase.QuestionFirebaseDataSource
import com.abloom.mery.data.firebase.asExternal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DefaultQuestionRepository @Inject constructor(
    private val firebaseDataSource: QuestionFirebaseDataSource
) : QuestionRepository {

    override fun getTodayRecommendationQuestion(): Flow<Question> = getEssentialQuestions()
        .map { it[0] }

    override suspend fun setTodayRecommendationQuestion(question: Question) {
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
