package com.abloom.mery.data.database

import androidx.room.TypeConverter
import java.time.LocalDate

class LocalDateConverter {

    @TypeConverter
    fun convertToDate(dateString: String): LocalDate = LocalDate.parse(dateString)

    @TypeConverter
    fun convertToDateString(date: LocalDate): String = date.toString()
}
