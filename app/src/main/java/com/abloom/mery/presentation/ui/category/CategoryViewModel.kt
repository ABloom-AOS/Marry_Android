package com.abloom.mery.presentation.ui.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abloom.domain.question.model.Category
import com.abloom.domain.question.model.Question
import com.abloom.domain.question.usecase.GetAvailableQuestionsUseCase
import com.abloom.domain.user.usecase.GetLoginUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    getLoginUserUseCase: GetLoginUserUseCase,
    getAvailableQuestionsUseCase: GetAvailableQuestionsUseCase
) : ViewModel() {

    val isLogin: StateFlow<Boolean?> = getLoginUserUseCase().map { it != null }
        .stateIn(
            initialValue = null,
            started = SharingStarted.WhileSubscribed(5_000),
            scope = viewModelScope
        )

    val questions: StateFlow<Map<Category, List<Question>>> = getAvailableQuestionsUseCase()
        .stateIn(
            initialValue = mapOf(),
            started = SharingStarted.WhileSubscribed(5_000),
            scope = viewModelScope
        )
}
