package com.abloom.mery.presentation.ui.category

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abloom.domain.question.model.Category
import com.abloom.domain.question.model.Question
import com.abloom.domain.question.usecase.GetAvailableQuestionsUseCase
import com.abloom.domain.question.usecase.GetQuestionsUseCase
import com.abloom.domain.user.usecase.GetLoginUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    getLoginUserUseCase: GetLoginUserUseCase,
    getAvailableQuestionsUseCase: GetAvailableQuestionsUseCase,
    getQuestionsUseCase: GetQuestionsUseCase
) : ViewModel() {

    val isLogin: StateFlow<Boolean> = getLoginUserUseCase().map { it != null }
        .stateIn(
            initialValue = true,
            started = SharingStarted.WhileSubscribed(5_000),
            scope = viewModelScope
        )

    val category: StateFlow<Category> = savedStateHandle
        .getStateFlow("category", CategoryArgs.FINANCE)
        .map(CategoryArgs::asDomain)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = Category.FINANCE
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    private val questions: StateFlow<Map<Category, List<Question>>> = isLogin
        .flatMapLatest { isLogin ->
            if (isLogin) getAvailableQuestionsUseCase() else getQuestionsUseCase()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyMap()
        )

    val currentQuestions: StateFlow<List<Question>> =
        combine(category, questions) { category, questions ->
            questions[category] ?: emptyList()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun selectCategory(categoryArgs: CategoryArgs) {
        savedStateHandle["category"] = categoryArgs
    }
}
