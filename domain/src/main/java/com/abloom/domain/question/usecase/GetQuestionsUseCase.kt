package com.abloom.domain.question.usecase

import com.abloom.domain.question.model.Category
import com.abloom.domain.question.model.Question
import com.abloom.domain.question.repository.QuestionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetQuestionsUseCase @Inject constructor(
    private val questionRepository: QuestionRepository
) {

    operator fun invoke(): Flow<Map<Category, List<Question>>> = questionRepository.getQuestionsFlow()
        .map { questions -> questions.groupBy { question -> question.category } }
}
