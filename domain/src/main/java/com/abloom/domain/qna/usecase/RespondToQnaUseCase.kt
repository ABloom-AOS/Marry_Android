package com.abloom.domain.qna.usecase

import com.abloom.domain.qna.model.Response
import com.abloom.domain.qna.model.UnfinishedResponseQna
import com.abloom.domain.qna.repository.ProspectiveCoupleQnaRepository
import javax.inject.Inject

class RespondToQnaUseCase @Inject constructor(
    private val qnaRepository: ProspectiveCoupleQnaRepository
) {

    suspend operator fun invoke(qna: UnfinishedResponseQna, response: Response) {
        qnaRepository.respondToQna(qna, response)
    }
}
