package com.abloom.mery.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abloom.domain.user.model.Authentication
import com.abloom.domain.user.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
) : ViewModel() {

    private val _event = MutableSharedFlow<LoginEvent>()
    val event: SharedFlow<LoginEvent> = _event.asSharedFlow()

    fun login(authentication: Authentication) = viewModelScope.launch {
        val isLoginSuccess = loginUseCase(authentication)
        if (!isLoginSuccess) _event.emit(LoginEvent.LoginFail(authentication))
    }
}
