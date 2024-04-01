package com.abloom.mery.presentation.ui.leave

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abloom.domain.user.usecase.LeaveUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeaveViewModel @Inject constructor(
    private val leaveUseCase: LeaveUseCase
) : ViewModel() {

    fun leave() = viewModelScope.launch {
        leaveUseCase()
    }
}
