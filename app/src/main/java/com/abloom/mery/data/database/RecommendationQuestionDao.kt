package com.abloom.mery.data.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface RecommendationQuestionDao {

    @Query("SELECT questionId FROM recommendation_questions WHERE userId = :userId AND date = :date")
    fun getRecommendationQuestionId(userId: String, date: LocalDate): Flow<Long?>

    @Upsert
    suspend fun setRecommendationQuestion(entity: RecommendationQuestionEntity)
}
