package com.abloom.mery.presentation.ui.profilemenu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abloom.domain.user.model.User
import com.abloom.domain.user.usecase.ChangeLoginUserMarriageDateUseCase
import com.abloom.domain.user.usecase.ChangeLoginUserNameUseCase
import com.abloom.domain.user.usecase.GetFianceUseCase
import com.abloom.domain.user.usecase.GetLoginUserUseCase
import com.abloom.domain.user.usecase.LogoutUseCase
import com.abloom.mery.BuildConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ProfileMenuViewModel @Inject constructor(
    getLoginUserUseCase: GetLoginUserUseCase,
    getFianceUseCase: GetFianceUseCase,
    private val changeLoginUserNameUseCase: ChangeLoginUserNameUseCase,
    private val changeLoginUserMarriageDateUseCase: ChangeLoginUserMarriageDateUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    val loginUser: StateFlow<User?> = getLoginUserUseCase().stateIn(
        initialValue = null,
        started = SharingStarted.WhileSubscribed(5_000),
        scope = viewModelScope
    )

    val loginUserDescriptionUiState: StateFlow<LoginUserDescriptionUiState> = loginUser
        .combine(getFianceUseCase()) { loginUser, fiance ->
            when {
                loginUser == null -> LoginUserDescriptionUiState.NotLogin
                fiance == null -> LoginUserDescriptionUiState.NotConnected
                else -> LoginUserDescriptionUiState.Fiance(fiance.name, fiance.sex)
            }
        }.stateIn(
            initialValue = LoginUserDescriptionUiState.NotLogin,
            started = SharingStarted.WhileSubscribed(5_000),
            scope = viewModelScope
        )

    val appVersion: String = BuildConfig.VERSION_NAME

    fun changeName(name: String) = viewModelScope.launch {
        changeLoginUserNameUseCase(name)
    }

    fun changeMarriageDate(marriageDate: LocalDate) = viewModelScope.launch {
        changeLoginUserMarriageDateUseCase(marriageDate)
    }

    fun logout() = viewModelScope.launch { logoutUseCase() }
}
