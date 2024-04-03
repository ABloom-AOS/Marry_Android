package com.abloom.mery.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abloom.domain.qna.model.Qna
import com.abloom.domain.qna.usecase.GetQnasUseCase
import com.abloom.domain.user.model.User
import com.abloom.domain.user.usecase.GetLoginUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    getLoginUserUseCase: GetLoginUserUseCase,
    getQnasUseCase: GetQnasUseCase,
) : ViewModel() {

    val loginUser: StateFlow<User?> = getLoginUserUseCase()
        .stateIn(
            initialValue = null,
            started = SharingStarted.WhileSubscribed(5_000),
            scope = viewModelScope
        )

    val isLogin: StateFlow<Boolean?> = loginUser.map { it != null }.stateIn(
        initialValue = null,
        started = SharingStarted.WhileSubscribed(5_000),
        scope = viewModelScope
    )

    val qnas: StateFlow<List<Qna>> = getQnasUseCase()
        .stateIn(
            initialValue = listOf(),
            started = SharingStarted.WhileSubscribed(5_000),
            scope = viewModelScope
        )
}
