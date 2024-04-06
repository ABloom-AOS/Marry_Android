package com.abloom.domain.question.usecase

import com.abloom.domain.qna.repository.ProspectiveCoupleQnaRepository
import com.abloom.domain.question.model.Question
import com.abloom.domain.question.repository.QuestionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.onEmpty
import javax.inject.Inject

class GetTodayRecommendationQuestionUseCase @Inject constructor(
    private val questionRepository: QuestionRepository,
    private val qnaRepository: ProspectiveCoupleQnaRepository
) {

    operator fun invoke(): Flow<Question?> =
        questionRepository.getTodayRecommendationQuestion().onEmpty {
            questionRepository.addTodayRecommendationQuestionByRandom()
        }

    private suspend fun QuestionRepository.addTodayRecommendationQuestionByRandom() {
        val unavailableQuestionIds = qnaRepository.getQnas()
            .last()
            .map { it.question.id }
            .toSet()
        val todayRecommendationQuestion = getTodayRecommendationQuestion(unavailableQuestionIds)
        this.setTodayRecommendationQuestion(todayRecommendationQuestion)
    }

    private suspend fun getTodayRecommendationQuestion(
        unavailableQuestionIds: Set<Long>
    ): Question = questionRepository.getEssentialQuestions()
        .last()
        .getRandomAvailableQuestion(unavailableQuestionIds)
        ?: questionRepository.getQuestions()
            .last()
            .getRandomAvailableQuestion(unavailableQuestionIds)
        ?: throw RuntimeException("사용자가 모든 질문에 대해 문답을 작성한 경우는 고려하지 않음")

    private fun List<Question>.getRandomAvailableQuestion(unavailableQuestionIds: Set<Long>): Question? =
        filter { question -> question.id !in unavailableQuestionIds }
            .randomOrNull()
}
