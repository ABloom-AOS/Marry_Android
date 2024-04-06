package com.abloom.domain.question.repository

import com.abloom.domain.question.model.Question
import kotlinx.coroutines.flow.Flow

interface QuestionRepository {

    fun getTodayRecommendationQuestion(): Flow<Question?>

    suspend fun setTodayRecommendationQuestion(question: Question)

    fun getEssentialQuestions(): Flow<List<Question>>

    fun getQuestions(): Flow<List<Question>>

    fun getQuestion(id: Long): Flow<Question>
}
