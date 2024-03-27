package com.abloom.domain.qna.usecase

import com.abloom.domain.qna.model.Qna1
import com.abloom.domain.qna.repository.ProspectiveCoupleQnaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetQnasUseCase @Inject constructor(
    private val qnaRepository: ProspectiveCoupleQnaRepository
) {

    operator fun invoke(): Flow<List<Qna1>> = qnaRepository.getQnas()
}
