package com.abloom.mery.presentation.ui.profilemenu

import com.abloom.domain.user.model.Sex

sealed interface LoginUserDescriptionUiState {
    data object NotLogin : LoginUserDescriptionUiState
    data object NotConnected : LoginUserDescriptionUiState
    data class Fiance(val name: String, val sex: Sex) : LoginUserDescriptionUiState
}
