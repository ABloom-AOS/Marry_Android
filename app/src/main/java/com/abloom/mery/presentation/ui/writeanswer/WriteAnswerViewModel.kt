package com.abloom.mery.presentation.ui.writeanswer

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abloom.domain.qna.model.Answer
import com.abloom.domain.qna.usecase.AnswerQnaUseCase
import com.abloom.domain.question.model.Question
import com.abloom.domain.question.usecase.GetQuestionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WriteAnswerViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getQuestionUseCase: GetQuestionUseCase,
    private val answerQnaUseCase: AnswerQnaUseCase
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val question: StateFlow<Question?> = savedStateHandle
        .getStateFlow(WriteAnswerFragment.KEY_QUESTION_ID, INVALID_QUESTION_ID)
        .filter { it != INVALID_QUESTION_ID }
        .flatMapLatest { getQuestionUseCase(it) }
        .stateIn(
            initialValue = null,
            started = SharingStarted.WhileSubscribed(5_000),
            scope = viewModelScope
        )

    val answer = MutableStateFlow("")

    val moveWithDeepLink = savedStateHandle.getStateFlow("moveWithDeepLink", true)

    fun answerQna() = viewModelScope.launch {
        val question = question.value ?: return@launch
        answerQnaUseCase(question.id, Answer(answer.value))
    }

    companion object {

        private const val INVALID_QUESTION_ID = -1L
        const val ANSWER_LIMIT_SIZE = 150
    }
}
