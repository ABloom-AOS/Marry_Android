package com.abloom.mery.presentation.ui.profilemenu.dialog.marriagedatechange

import android.view.View
import androidx.annotation.Dimension
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.abloom.mery.R
import com.abloom.mery.databinding.LayoutMarriagedatechangedialogDayBinding
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.view.ViewContainer
import java.time.LocalDate

class CalendarDayViewHolder(
    view: View,
    onDayClick: (day: CalendarDay) -> Unit,
) : ViewContainer(view) {

    private val binding by lazy { LayoutMarriagedatechangedialogDayBinding.bind(view) }

    init {
        binding.onDayClick = { onDayClick(it) }
    }

    fun bind(day: CalendarDay, isSelected: Boolean) {
        binding.day = day

        updateFontSize(isSelected)
        updateFont(isSelected)
        updateBackground(isSelected)
        updateTextColor(isSelected)
    }

    private fun updateFontSize(isSelected: Boolean) {
        val fontSize = if (isSelected) 24f else 20f
        binding.tvMarriagedatechangedialogDay.setTextSize(Dimension.SP, fontSize)
    }

    private fun updateFont(isSelected: Boolean) {
        val fontRes =
            if (isSelected) R.font.sf_pro_display_medium else R.font.sf_pro_display_regular
        binding.tvMarriagedatechangedialogDay.typeface =
            ResourcesCompat.getFont(view.context, fontRes)
    }

    private fun updateBackground(isSelected: Boolean) {
        binding.tvMarriagedatechangedialogDay.background = if (isSelected) {
            ContextCompat.getDrawable(
                view.context,
                R.drawable.bg_marriagedatechangedialog_selected_date
            )
        } else {
            null
        }
    }

    private fun updateTextColor(isSelected: Boolean) {
        val day = binding.day ?: return
        val colorRes =
            if (isSelected || day.date == LocalDate.now()) R.color.primary_60 else R.color.black
        val color = ContextCompat.getColor(view.context, colorRes)
        binding.tvMarriagedatechangedialogDay.setTextColor(color)
    }
}
