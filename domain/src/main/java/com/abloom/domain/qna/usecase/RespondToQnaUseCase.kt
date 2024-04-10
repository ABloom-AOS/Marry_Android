package com.abloom.domain.qna.usecase

import com.abloom.domain.qna.model.Qna
import com.abloom.domain.qna.model.Response
import com.abloom.domain.qna.model.UnfinishedResponseQna
import com.abloom.domain.qna.repository.ProspectiveCoupleQnaRepository
import javax.inject.Inject

class RespondToQnaUseCase @Inject constructor(
    private val qnaRepository: ProspectiveCoupleQnaRepository
) {

    suspend operator fun invoke(qna: UnfinishedResponseQna, response: Response) {
        require(qna.loginUserResponse == null) { "로그인 유저의 반응이 추가되어 있습니다." }
        qnaRepository.respondToQna(qna, response)
    }
}
