package com.abloom.domain.question.usecase

import com.abloom.domain.qna.repository.ProspectiveCoupleQnaRepository
import com.abloom.domain.question.model.Category
import com.abloom.domain.question.model.Question
import com.abloom.domain.question.repository.QuestionRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAvailableQuestionsUseCase @Inject constructor(
    private val questionRepository: QuestionRepository,
    private val qnaRepository: ProspectiveCoupleQnaRepository
) {

    /**
     * Qna로 만들지 않은 질문만 사용가능합니다.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<Map<Category, List<Question>>> =
        qnaRepository.getQnasFlow().flatMapLatest { qnas ->
            val unavailableQuestionIds = qnas.map { it.question.id }.toSet()

            questionRepository.getQuestionsFlow().map { questions ->
                questions.filter { question -> question.id !in unavailableQuestionIds }
                    .groupBy { question -> question.category }
            }
        }
}
