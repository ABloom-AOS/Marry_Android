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

    var subCheckBox1 = MutableStateFlow(false)
    var subCheckBox2 = MutableStateFlow(false)
    var subCheckBox3 = MutableStateFlow(false)
    var subCheckBox4 = MutableStateFlow(false)

    var allCheckBox: StateFlow<Boolean> = combine(
        subCheckBox1, subCheckBox2, subCheckBox3, subCheckBox4
    ) { checkBoxStates ->
        checkBoxStates.all { it }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun selectSex(sex: Sex) {
        _selectedSex.value = sex
    }

    fun selectMarriageDate(marriageDate: LocalDate) {
        _selectedMarriageDate.value = marriageDate
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
