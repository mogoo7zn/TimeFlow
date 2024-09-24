package cn.edu.ustc.timeflow.converter

import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Calendar

object CalendarLocalDateTimeConverter {
    fun toLocalDateTime(calendar: Calendar): LocalDateTime {
        return LocalDateTime.ofInstant(calendar.toInstant(), calendar.timeZone.toZoneId())
    }

    fun toCalendar(localDateTime: LocalDateTime, zoneId: ZoneId = ZoneId.systemDefault()): Calendar {
        val calendar = Calendar.getInstance()
        calendar.timeZone = java.util.TimeZone.getTimeZone(zoneId)
        calendar.time = java.util.Date.from(localDateTime.atZone(zoneId).toInstant())
        return calendar
    }
}