package cn.edu.ustc.timeflow.converter

import androidx.room.TypeConverter
import java.time.Duration

public class DurationConverter {
    @TypeConverter
    fun toDuration(value: String?): Duration? {
        return value?.let { Duration.parse(it) }
    }
    @TypeConverter
    fun fromDuration(duration: Duration?): String? {
        return duration?.toString()
    }
}