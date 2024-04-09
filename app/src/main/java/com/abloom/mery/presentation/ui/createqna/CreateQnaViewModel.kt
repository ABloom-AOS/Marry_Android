package com.abloom.mery.presentation.ui.createqna

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abloom.domain.question.model.Category
import com.abloom.domain.question.model.Question
import com.abloom.domain.question.usecase.GetTodayRecommendationQuestionUseCase
import com.abloom.domain.user.usecase.GetLoginUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class CreateQnaViewModel @Inject constructor(
    getLoginUserUseCase: GetLoginUserUseCase,
    getTodayRecommendationQuestionUseCase: GetTodayRecommendationQuestionUseCase
) : ViewModel() {

    val isLogin: StateFlow<Boolean?> = getLoginUserUseCase().map { it != null }
        .stateIn(
            initialValue = null,
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed()
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val todayRecommendationQuestion: StateFlow<Question?> = isLogin.filterNotNull()
        .flatMapLatest { isLogin ->
            if (isLogin) getTodayRecommendationQuestionUseCase() else flowOf(DUMMY_QUESTION)
        }.stateIn(
            initialValue = null,
            started = SharingStarted.WhileSubscribed(5_000),
            scope = viewModelScope
        )

    companion object {

        private val DUMMY_QUESTION = Question(
            id = 6L,
            category = Category.COMMUNICATION,
            content = "상대방에게 들었던 말 중에 가장 기분이 좋았던 말은 무엇인가요?",
        )
    }
}
