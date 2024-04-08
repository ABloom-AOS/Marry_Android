package com.abloom.domain.question.usecase

import com.abloom.domain.question.model.Question
import com.abloom.domain.question.repository.QuestionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetQuestionUseCase @Inject constructor(
    private val questionRepository: QuestionRepository
) {

    operator fun invoke(questionId: Long): Flow<Question> =
        questionRepository.getQuestionFlow(questionId)
}
