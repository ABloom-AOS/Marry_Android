package com.abloom.domain.question.repository

import com.abloom.domain.question.model.Question
import kotlinx.coroutines.flow.Flow

interface QuestionRepository {

    fun getTodayRecommendationQuestion(): Flow<Question?>

    suspend fun setTodayRecommendationQuestion(questionId: Long)

    suspend fun getEssentialQuestionIds(): List<Long>

    fun getQuestionsFlow(): Flow<List<Question>>

    suspend fun getQuestions(): List<Question>

    fun getQuestion(id: Long): Flow<Question>
}
