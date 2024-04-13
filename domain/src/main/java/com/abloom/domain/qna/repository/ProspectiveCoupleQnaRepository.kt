package com.abloom.domain.qna.repository

import com.abloom.domain.qna.model.Answer
import com.abloom.domain.qna.model.FinishedQna
import com.abloom.domain.qna.model.Qna
import com.abloom.domain.qna.model.Response
import com.abloom.domain.qna.model.UnfinishedResponseQna
import kotlinx.coroutines.flow.Flow

/**
 * 로그인 유저의 예비 부부의 문답에 대해서만 조회하고 변경할 수 있습니다.
 */
interface ProspectiveCoupleQnaRepository {

    fun getQnasFlow(): Flow<List<Qna>>

    fun getQnaFlow(questionId: Long): Flow<Qna>

    /**
     * 만약 해당 질문에 대한 문답이 없으면 문답을 생성합니다.
     * 만약 이미 질문에 대한 대답이 있으면 대답을 갱신하지 않고 새 대답을 무시합니다.
     */
    suspend fun answerQna(questionId: Long, answer: Answer)

    suspend fun respondToQna(qna: UnfinishedResponseQna, response: Response)

    suspend fun changeResponse(qna: FinishedQna, response: Response)
}
