package com.abloom.domain.qna.model

import com.abloom.domain.question.model.Question
import com.abloom.domain.user.model.User
import java.time.LocalDateTime

data class FinishedQna(
    override val question: Question,
    override val createdAt: LocalDateTime,
    override val loginUser: User,
    val fiance: User,
    val loginUserAnswer: Answer,
    val fianceAnswer: Answer,
    val loginUserResponse: Response,
    val fianceResponse: Response,
) : Qna() {

    val responseResult: ResponseResult = ResponseResult.of(loginUserResponse, fianceResponse)

    override fun compareTo(other: Qna): Int {
        if (other !is FinishedQna) return 1
        return super.compareTo(other)
    }
}
