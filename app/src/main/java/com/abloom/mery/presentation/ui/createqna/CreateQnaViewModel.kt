package com.abloom.mery.presentation.ui.createqna

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abloom.domain.question.model.Question
import com.abloom.domain.question.usecase.GetTodayRecommendationQuestionUseCase
import com.abloom.domain.user.usecase.GetLoginUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class CreateQnaViewModel @Inject constructor(
    getLoginUserUseCase: GetLoginUserUseCase,
    getTodayRecommendationQuestionUseCase: GetTodayRecommendationQuestionUseCase
) : ViewModel() {

    val todayRecommendationQuestion: StateFlow<Question?> = getTodayRecommendationQuestionUseCase()
        .stateIn(
            initialValue = null,
            started = SharingStarted.WhileSubscribed(5_000),
            scope = viewModelScope
        )

    val isLogin: StateFlow<Boolean> = getLoginUserUseCase().map { it != null }
        .stateIn(
            initialValue = false,
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed()
        )
}
