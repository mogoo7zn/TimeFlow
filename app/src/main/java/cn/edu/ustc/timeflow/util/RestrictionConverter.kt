package cn.edu.ustc.timeflow.util

import androidx.room.TypeConverter
import cn.edu.ustc.timeflow.restriction.FixedTimeRestriction
import cn.edu.ustc.timeflow.restriction.RepeatRestriction
import cn.edu.ustc.timeflow.restriction.Restriction
import cn.edu.ustc.timeflow.restriction.TimeRestriction

class RestrictionConverter {
    @TypeConverter
    fun fromString(value: String): List<Restriction> {
        val list = mutableListOf<Restriction>()
        val t = value.split(",")
        for (s in t) {
            list.add(RestrictionFactory(s).create())
        }
        return list
    }
    @TypeConverter
    fun toString(value: List<Restriction>): String {
        return value.joinToString(",") { it.toString() }
    }
}

class RestrictionFactory(private var code: String) {
    fun create(): Restriction {

        val t = code.split(":")
        return when (t[0]) {
            "TimeRestriction" -> {
                TimeRestriction(t[1])
            }

            "FixedTimeRestriction" -> {
                FixedTimeRestriction(t[1])
            }
            "RepeatRestriction" -> {
                RepeatRestriction(t[1])
            }
            else -> {
                throw IllegalArgumentException("Unknown type")
            }
        }

    }


}