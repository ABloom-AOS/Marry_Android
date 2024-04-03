package com.abloom.mery.presentation.ui.home

import com.abloom.domain.user.model.Authentication

sealed interface LoginEvent {
    data class LoginFail(val authentication: Authentication) : LoginEvent
}
