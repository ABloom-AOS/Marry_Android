package com.abloom.mery.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abloom.mery.presentation.common.EventFlow
import com.abloom.mery.presentation.common.MutableEventFlow
import com.abloom.mery.presentation.common.asEventFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _loginEvent = MutableEventFlow<LoginEvent>()
    val loginEvent: EventFlow<LoginEvent> = _loginEvent.asEventFlow()

    private val _answerEvent = MutableEventFlow<AnswerEvent>()
    val answerEvent: EventFlow<AnswerEvent> = _answerEvent.asEventFlow()


    var wasClosedQuestionFactoryPopup = false

    init {
        viewModelScope.launch { _loginEvent.emit(LoginEvent) }
    }

    fun dispatchLoginEvent() = viewModelScope.launch {
        _loginEvent.emit(LoginEvent)
    }

    fun dispatchReviewEvent() = viewModelScope.launch {
        _answerEvent.emit(AnswerEvent)
    }
}

data object LoginEvent
data object AnswerEvent
