package com.abloom.mery.presentation.ui.profilemenu.marriagedatechange

import androidx.lifecycle.ViewModel
import com.kizitonwose.calendar.core.yearMonth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import java.time.YearMonth

class MarriageDateChangeViewModel : ViewModel() {

    private val _isCalendarState = MutableStateFlow(true)
    val isCalendarState: StateFlow<Boolean> = _isCalendarState.asStateFlow()

    private val _visibleMonth = MutableStateFlow(LocalDate.now().yearMonth)
    val visibleMonth: StateFlow<YearMonth> = _visibleMonth.asStateFlow()

    private val _selectedDate = MutableStateFlow<LocalDate?>(null)
    val selectedDate: StateFlow<LocalDate?> = _selectedDate.asStateFlow()

    fun toggleCalendarState() {
        _isCalendarState.value = !_isCalendarState.value
    }

    fun setVisibleMonth(yearMonth: YearMonth) {
        _visibleMonth.value = yearMonth
    }

    fun selectDate(date: LocalDate) {
        _selectedDate.value = date
    }
}
