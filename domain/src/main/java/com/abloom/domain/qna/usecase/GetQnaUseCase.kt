package com.abloom.domain.qna.usecase

import com.abloom.domain.qna.model.Qna1
import com.abloom.domain.qna.repository.ProspectiveCoupleQnaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetQnaUseCase @Inject constructor(
    private val qnaRepository: ProspectiveCoupleQnaRepository
) {

    operator fun invoke(questionId: Long): Flow<Qna1> =
        qnaRepository.getQna(questionId)
}
