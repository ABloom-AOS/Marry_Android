package com.abloom.mery.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity("recommendation_questions")
data class RecommendationQuestionEntity(
    @PrimaryKey
    val userId: String,
    val date: LocalDate,
    val questionId: Long
) {

    companion object {

        const val NOT_LOGIN_ID = "NOT_LOGIN_ID"

        fun create(
            userId: String?,
            date: LocalDate,
            questionId: Long
        ) = RecommendationQuestionEntity(
            userId = userId ?: NOT_LOGIN_ID,
            date = date,
            questionId = questionId
        )
    }
}
