package com.abloom.mery.presentation.ui.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abloom.domain.user.model.Authentication
import com.abloom.domain.user.model.Sex
import com.abloom.domain.user.usecase.JoinUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val joinUseCase: JoinUseCase
) : ViewModel() {

    private val _selectedSex = MutableStateFlow<Sex?>(null)
    val selectedSex = _selectedSex.asStateFlow()

    private val _selectedMarriageDate = MutableStateFlow<LocalDate>(LocalDate.now())
    val selectedMarriage = _selectedMarriageDate.asStateFlow()

    val name = MutableStateFlow("")

    var isAgeChecked = MutableStateFlow(false)
    var isTermsOfUseChecked = MutableStateFlow(false)
    var isSensitivePrivacyChecked = MutableStateFlow(false)
    var isPrivacyPolicyChecked = MutableStateFlow(false)

    val isAllChecked: StateFlow<Boolean> = combine(
        isAgeChecked, isTermsOfUseChecked, isSensitivePrivacyChecked, isPrivacyPolicyChecked
    ) { checkBoxesState ->
        checkBoxesState.all { it }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = false
    )

    fun selectSex(sex: Sex) {
        _selectedSex.value = sex
    }

    fun selectMarriageDate(marriageDate: LocalDate) {
        _selectedMarriageDate.value = marriageDate
    }

    fun toggleAllCheckState() {
        if (isAllChecked.value) uncheckAll() else checkAll()
    }

    private fun uncheckAll() {
        isAgeChecked.value = false
        isTermsOfUseChecked.value = false
        isSensitivePrivacyChecked.value = false
        isPrivacyPolicyChecked.value = false
    }

    private fun checkAll() {
        isAgeChecked.value = true
        isTermsOfUseChecked.value = true
        isSensitivePrivacyChecked.value = true
        isPrivacyPolicyChecked.value = true
    }

    fun join(authentication: Authentication) = viewModelScope.launch {
        val selectedSex = _selectedSex.value ?: return@launch
        joinUseCase(
            authentication = authentication,
            sex = selectedSex,
            marriageDate = _selectedMarriageDate.value,
            name = name.value
        )
    }
}
