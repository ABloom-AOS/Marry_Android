package com.abloom.mery.presentation.common.bindingadapter

import android.widget.TextView
import androidx.annotation.FontRes
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import com.abloom.mery.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@BindingAdapter("app:date_text")
fun TextView.setDateText(date: LocalDate) {
    text = date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG))
}

@BindingAdapter("app:font")
fun TextView.setFont(fontType: FontType) {
    typeface = ResourcesCompat.getFont(context, fontType.fontRes)
}

enum class FontType(@FontRes val fontRes: Int) {
    NANUM_SQUARE_NEO_REGULAR(R.font.nanum_square_neo_regular),
    NANUM_SQUARE_NEO_BOLD(R.font.nanum_square_neo_bold),
    NANUM_SQUARE_NEO_EXTRA_BOLD(R.font.nanum_square_neo_extra_bold)
}
