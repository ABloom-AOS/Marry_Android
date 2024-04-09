package com.abloom.domain.qna.model

import com.abloom.domain.question.model.Question
import com.abloom.domain.user.model.User
import java.time.LocalDateTime

data class UnfinishedAnswerQna(
    override val question: Question,
    override val createdAt: LocalDateTime,
    override val loginUser: User,
    val fiance: User,
    val loginUserAnswer: Answer? = null,
    val fianceAnswer: Answer? = null
) : Qna() {

    override fun compareTo(other: Qna): Int = when (other) {
        is UnconnectedQna -> 1
        is UnfinishedAnswerQna -> when {
            loginUserAnswer == null && other.loginUserAnswer != null -> -1
            loginUserAnswer != null && other.loginUserAnswer == null -> 1
            else -> super.compareTo(other)
        }

        is UnfinishedResponseQna -> when {
            loginUserAnswer == null && other.loginUserResponse != null -> -1
            loginUserAnswer != null && other.loginUserResponse == null -> 1
            else -> super.compareTo(other)
        }

        is FinishedQna -> -1
    }
}
