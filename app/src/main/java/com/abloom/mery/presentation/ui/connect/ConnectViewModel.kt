package com.abloom.mery.presentation.ui.connect

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abloom.domain.user.model.User
import com.abloom.domain.user.usecase.ConnectWithFianceUseCase
import com.abloom.domain.user.usecase.GetFianceUseCase
import com.abloom.domain.user.usecase.GetLoginUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConnectViewModel @Inject constructor(
    getLoginUserUseCase: GetLoginUserUseCase,
    getFianceUseCase: GetFianceUseCase,
    private val connectWithFianceUseCase: ConnectWithFianceUseCase
) : ViewModel() {

    val loginUser: StateFlow<User?> = getLoginUserUseCase().stateIn(
        initialValue = null,
        started = SharingStarted.WhileSubscribed(5_000),
        scope = viewModelScope
    )

    val fiance: StateFlow<User?> = getFianceUseCase().stateIn(
        initialValue = null,
        started = SharingStarted.WhileSubscribed(5_000),
        scope = viewModelScope
    )

    private val _isConnecting = MutableStateFlow(false)
    val isConnecting: StateFlow<Boolean> = _isConnecting.asStateFlow()

    private val _event = MutableSharedFlow<ConnectEvent>()
    val event: SharedFlow<ConnectEvent> = _event.asSharedFlow()

    fun connectWithFiance(invitationCode: String) = viewModelScope.launch {
        _isConnecting.value = true
        val connectResult = connectWithFianceUseCase(invitationCode)
        _isConnecting.value = false
        if (connectResult) _event.emit(ConnectEvent.ConnectSuccess) else _event.emit(ConnectEvent.ConnectFail)
    }
}
