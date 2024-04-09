package com.abloom.mery.presentation.ui.home

import com.abloom.domain.user.model.User

sealed interface UserUiState {
    data object Loading : UserUiState
    data class Login(val user: User) : UserUiState
    data object NotLogin : UserUiState

    companion object {

        fun from(user: User?): UserUiState = if (user == null) NotLogin else Login(user)
    }
}
