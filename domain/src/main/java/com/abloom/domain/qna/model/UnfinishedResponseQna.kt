package com.abloom.domain.qna.model

import com.abloom.domain.question.model.Question
import com.abloom.domain.user.model.User
import java.time.LocalDateTime

data class UnfinishedResponseQna(
    override val question: Question,
    override val createdAt: LocalDateTime,
    override val loginUser: User,
    val fiance: User,
    val loginUserAnswer: Answer,
    val fianceAnswer: Answer,
    val loginUserResponse: Response? = null,
    val fianceResponse: Response? = null,
) : Qna() {

    override fun compareTo(other: Qna): Int = when (other) {
        is UnconnectedQna -> 1
        is UnfinishedAnswerQna -> when {
            loginUserResponse == null && other.loginUserAnswer != null -> -1
            loginUserResponse != null && other.loginUserAnswer == null -> 1
            else -> super.compareTo(other)
        }

        is UnfinishedResponseQna -> when {
            loginUserResponse == null && other.loginUserResponse != null -> -1
            loginUserResponse != null && other.loginUserResponse == null -> 1
            else -> super.compareTo(other)
        }

        is FinishedQna -> -1
    }
}
