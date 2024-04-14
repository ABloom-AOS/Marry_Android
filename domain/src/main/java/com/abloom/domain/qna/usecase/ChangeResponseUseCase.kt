package com.abloom.domain.qna.usecase

import com.abloom.domain.qna.model.FinishedQna
import com.abloom.domain.qna.model.Response
import com.abloom.domain.qna.repository.ProspectiveCoupleQnaRepository
import javax.inject.Inject

class ChangeResponseUseCase @Inject constructor(
    private val qnaRepository: ProspectiveCoupleQnaRepository
) {

    suspend operator fun invoke(qna: FinishedQna, response: Response) {
        qnaRepository.changeResponse(qna, response)
    }
}
