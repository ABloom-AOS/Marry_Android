package com.abloom.mery.presentation.ui.category

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abloom.domain.question.model.Question
import com.abloom.domain.question.usecase.GetAvailableQuestionsUseCase
import com.abloom.domain.user.usecase.GetLoginUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    getLoginUserUseCase: GetLoginUserUseCase,
    getAvailableQuestionsUseCase: GetAvailableQuestionsUseCase
) : ViewModel() {

    val isLogin: StateFlow<Boolean?> = getLoginUserUseCase().map { it != null }
        .stateIn(
            initialValue = true,
            started = SharingStarted.WhileSubscribed(5_000),
            scope = viewModelScope
        )

    val category: StateFlow<CategoryArgs> =
        savedStateHandle.getStateFlow("category", CategoryArgs.FINANCE)

    @OptIn(ExperimentalCoroutinesApi::class)
    val questions: StateFlow<List<Question>> = category
        .map(CategoryArgs::asDomain)
        .flatMapLatest { category ->
            getAvailableQuestionsUseCase().map { questions -> questions[category] ?: emptyList() }
        }.stateIn(
            initialValue = emptyList(),
            started = SharingStarted.WhileSubscribed(5_000),
            scope = viewModelScope
        )

    fun selectCategory(categoryArgs: CategoryArgs) {
        savedStateHandle["category"] = categoryArgs
    }
}
