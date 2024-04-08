package com.abloom.mery.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abloom.domain.qna.model.Qna
import com.abloom.domain.qna.usecase.GetQnasUseCase
import com.abloom.domain.user.model.Authentication
import com.abloom.domain.user.usecase.GetLoginUserUseCase
import com.abloom.domain.user.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    getLoginUserUseCase: GetLoginUserUseCase,
    getQnasUseCase: GetQnasUseCase,
    private val loginUseCase: LoginUseCase,
) : ViewModel() {

    val loginUser: StateFlow<UserUiState> = getLoginUserUseCase()
        .map { user -> UserUiState.from(user) }
        .stateIn(
            initialValue = UserUiState.Loading,
            started = SharingStarted.WhileSubscribed(5_000),
            scope = viewModelScope
        )

    val isLogin: StateFlow<Boolean?> = loginUser
        .filter { it !is UserUiState.Loading }
        .map { it is UserUiState.Login }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val qnas: StateFlow<List<Qna>> = isLogin.filterNotNull()
        .flatMapLatest { isLogin ->
            if (isLogin) getQnasUseCase() else flow { emit(emptyList()) }
        }.stateIn(
            initialValue = listOf(),
            started = SharingStarted.WhileSubscribed(5_000),
            scope = viewModelScope
        )

    private val _event = MutableSharedFlow<HomeEvent>()
    val event: SharedFlow<HomeEvent> = _event.asSharedFlow()

    fun login(authentication: Authentication) = viewModelScope.launch {
        val isLoginSuccess = loginUseCase(authentication)
        if (!isLoginSuccess) _event.emit(HomeEvent.LoginFail(authentication))
    }
}
