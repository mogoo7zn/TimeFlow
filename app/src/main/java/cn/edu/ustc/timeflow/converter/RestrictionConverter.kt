package cn.edu.ustc.timeflow.converter

import androidx.room.TypeConverter
import cn.edu.ustc.timeflow.restriction.AmountRestriction
import cn.edu.ustc.timeflow.restriction.FixedTimeRestriction
import cn.edu.ustc.timeflow.restriction.IntervalRestriction
import cn.edu.ustc.timeflow.restriction.Restriction
import cn.edu.ustc.timeflow.restriction.TimeRestriction

class RestrictionConverter {
    @TypeConverter
    fun fromString(value: String): List<Restriction> {

        val list = mutableListOf<Restriction>()
        if (value.isEmpty()) {
            return list
        }
        val t = value.split(",")
        for (s in t) {
            list.add(RestrictionFactory(s).create())
        }
        return list
    }
    @TypeConverter
    fun toString(value: List<Restriction>): String {
        return value.joinToString(",") { it.coding() }
    }
}

class RestrictionFactory(private var code: String) {
    fun create(): Restriction {

        val t = code.split("=")
        return when (t[0]) {
            "TimeRestriction" -> {
                TimeRestriction(t[1])
            }

            "FixedTimeRestriction" -> {
                FixedTimeRestriction(t[1])
            }

            "AmountRestriction" ->{
                AmountRestriction(t[1])
            }

            "IntervalRestriction"->{
                IntervalRestriction(t[1])
            }


            else -> {
                throw IllegalArgumentException("Unknown type")
            }
        }

    }


}