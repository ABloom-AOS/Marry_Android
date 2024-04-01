package com.abloom.mery.presentation.ui.profilemenu.marriagedatechange

import android.view.View
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.view.MonthDayBinder
import java.time.LocalDate

class CalendarDayAdapter(
    var selectedDate: LocalDate,
    private val onDayClick: (day: CalendarDay) -> Unit
) : MonthDayBinder<CalendarDayViewHolder> {

    override fun create(view: View): CalendarDayViewHolder = CalendarDayViewHolder(view, onDayClick)

    override fun bind(container: CalendarDayViewHolder, data: CalendarDay) {
        container.bind(day = data, isSelected = selectedDate == data.date)
    }
}
