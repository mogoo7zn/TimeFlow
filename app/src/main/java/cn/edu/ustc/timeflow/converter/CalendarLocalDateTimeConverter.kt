package cn.edu.ustc.timeflow.converter

import java.time.LocalDateTime
import java.util.Calendar

object CalendarLocalDateTimeConverter {
    fun toLocalDateTime(calendar: Calendar): LocalDateTime {
        return LocalDateTime.of(
            calendar[Calendar.YEAR],
            calendar[Calendar.MONTH] + 1,
            calendar[Calendar.DAY_OF_MONTH],
            calendar[Calendar.HOUR_OF_DAY],
            calendar[Calendar.MINUTE],
            calendar[Calendar.SECOND]
        )
    }

    fun toCalendar(localDateTime: LocalDateTime): Calendar {
        val calendar = Calendar.getInstance()
        calendar[localDateTime.year, localDateTime.monthValue - 1, localDateTime.dayOfMonth, localDateTime.hour, localDateTime.minute] =
            localDateTime.second
        return calendar
    }
}