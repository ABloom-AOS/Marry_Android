package com.abloom.domain.qna.model

import com.abloom.domain.question.model.Question
import com.abloom.domain.user.model.User
import java.time.LocalDateTime

sealed class Qna : Comparable<Qna> {

    abstract val question: Question
    abstract val createdAt: LocalDateTime
    abstract val loginUser: User

    override fun compareTo(other: Qna): Int = createdAt.compareTo(other.createdAt)

    companion object {

        fun create(
            question: Question,
            createdAt: LocalDateTime,
            loginUser: User,
            fiance: User? = null,
            loginUserAnswer: Answer? = null,
            fianceAnswer: Answer? = null,
            loginUserResponse: Response? = null,
            fianceResponse: Response? = null,
        ): Qna = when {
            fiance == null -> UnconnectedQna(
                question = question,
                createdAt = createdAt,
                loginUser = loginUser,
                loginUserAnswer = loginUserAnswer
                    ?: throw IllegalArgumentException("연결되어 있지 않은 상태에서 답변 없이 문답을 생성할 수 없습니다.")
            )

            loginUserAnswer == null || fianceAnswer == null -> UnfinishedAnswerQna(
                question = question,
                createdAt = createdAt,
                loginUser = loginUser,
                fiance = fiance,
                loginUserAnswer = loginUserAnswer,
                fianceAnswer = fianceAnswer
            )

            loginUserResponse == null || fianceResponse == null -> UnfinishedResponseQna(
                question = question,
                createdAt = createdAt,
                loginUser = loginUser,
                fiance = fiance,
                loginUserAnswer = loginUserAnswer,
                fianceAnswer = fianceAnswer,
                loginUserResponse = loginUserResponse,
                fianceResponse = fianceResponse
            )

            else -> FinishedQna(
                question = question,
                createdAt = createdAt,
                loginUser = loginUser,
                fiance = fiance,
                loginUserAnswer = loginUserAnswer,
                fianceAnswer = fianceAnswer,
                loginUserResponse = loginUserResponse,
                fianceResponse = fianceResponse
            )
        }
    }
}








