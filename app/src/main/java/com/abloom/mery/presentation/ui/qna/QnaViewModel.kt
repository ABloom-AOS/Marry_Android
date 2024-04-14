package com.abloom.mery.presentation.ui.qna

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abloom.domain.qna.model.FinishedQna
import com.abloom.domain.qna.model.Qna
import com.abloom.domain.qna.model.Response
import com.abloom.domain.qna.model.UnfinishedResponseQna
import com.abloom.domain.qna.usecase.ChangeResponseUseCase
import com.abloom.domain.qna.usecase.GetQnaUseCase
import com.abloom.domain.qna.usecase.RespondToQnaUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QnaViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getQnaUseCase: GetQnaUseCase,
    private val respondToQnaUseCase: RespondToQnaUseCase,
    private val changeResponseUseCase: ChangeResponseUseCase
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val qna: StateFlow<Qna?> = savedStateHandle
        .getStateFlow(QnaFragment.KEY_QUESTION_ID, INVALID_QUESTION_ID)
        .filter { it != INVALID_QUESTION_ID }
        .flatMapLatest { getQnaUseCase(it) }
        .stateIn(
            initialValue = null,
            started = SharingStarted.WhileSubscribed(5_000),
            scope = viewModelScope
        )

    private val selectedResponse = MutableStateFlow<Response?>(null)

    val currentResponse: StateFlow<Response?> =
        combine(qna, selectedResponse) { qna, selectedResponse ->
            selectedResponse
                ?: if (qna is UnfinishedResponseQna) qna.loginUserResponse
                else if (qna is FinishedQna) qna.loginUserResponse
                else null
        }.stateIn(
            initialValue = null,
            started = SharingStarted.WhileSubscribed(5_000),
            scope = viewModelScope
        )

    fun selectResponse(response: Response) {
        selectedResponse.value = response
    }

    fun resetResponse() {
        selectedResponse.value = null
    }

    fun respondToQna(response: Response) = viewModelScope.launch {
        val qna = qna.value ?: return@launch
        if (qna is UnfinishedResponseQna) respondToQnaUseCase(qna, response)
        else if (qna is FinishedQna) changeResponseUseCase(qna, response)
    }

    companion object {

        private const val INVALID_QUESTION_ID = -1L
    }
}
