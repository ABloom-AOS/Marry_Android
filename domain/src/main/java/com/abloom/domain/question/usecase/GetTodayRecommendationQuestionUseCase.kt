package com.abloom.domain.question.usecase

import com.abloom.domain.qna.repository.ProspectiveCoupleQnaRepository
import com.abloom.domain.question.model.Question
import com.abloom.domain.question.repository.QuestionRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class GetTodayRecommendationQuestionUseCase @Inject constructor(
    private val questionRepository: QuestionRepository,
    private val qnaRepository: ProspectiveCoupleQnaRepository
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<Question?> = qnaRepository.getQnasFlow()
        .flatMapLatest { qnas ->
            val unavailableQuestionIds = qnas.map { qna -> qna.question.id }.toSet()
            questionRepository.getTodayRecommendationQuestionFlow()
                .onEach { question ->
                    if (question.isAvailable(unavailableQuestionIds)) return@onEach
                    refreshTodayRecommendationQuestion(unavailableQuestionIds)
                }
                .filter { question -> question.isAvailable(unavailableQuestionIds) }
        }

    private fun Question?.isAvailable(unavailableQuestionIds: Set<Long>): Boolean =
        this != null && this.id !in unavailableQuestionIds

    private suspend fun refreshTodayRecommendationQuestion(unavailableQuestionIds: Set<Long>) {
        // 로그인하지 않은 상태에서 필수 질문 아이디 목록을 조회하면 설정된 firestore 규칙에 의해 에러가 발생합니다.
        val essentialQuestionIds = runCatching { questionRepository.getEssentialQuestionIds() }
            .getOrDefault(listOf())

        val todayRecommendationQuestionId = essentialQuestionIds
            .randomAvailableQuestionId(unavailableQuestionIds)
            ?: questionRepository.getQuestions().randomAvailableQuestionId(unavailableQuestionIds)
            ?: throw AssertionError("사용자가 모든 질문에 대해 문답을 작성한 경우는 고려하지 않음")

        questionRepository.setTodayRecommendationQuestion(todayRecommendationQuestionId)
    }

    @JvmName("randomAvailableQuestionIdOfIds")
    private fun List<Long>.randomAvailableQuestionId(unavailableQuestionIds: Set<Long>): Long? =
        filter { it !in unavailableQuestionIds }.randomOrNull()

    private fun List<Question>.randomAvailableQuestionId(unavailableQuestionIds: Set<Long>): Long? =
        map { it.id }.randomAvailableQuestionId(unavailableQuestionIds)
}
