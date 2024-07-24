package cn.edu.ustc.timeflow.util

import androidx.room.TypeConverter
import java.time.LocalDateTime

public class DateTimeConverter {
    @TypeConverter
    fun toLocalDateTime(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it) }
    }

    @TypeConverter
    fun fromLocalDateTime(date: LocalDateTime?): String? {
        return date?.toString()
    }
}
