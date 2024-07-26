package com.example.timeflow

import cn.edu.ustc.timeflow.bean.Action
import cn.edu.ustc.timeflow.bean.FixedTimeRestriction
import cn.edu.ustc.timeflow.bean.RepeatRestriction
import cn.edu.ustc.timeflow.bean.Restriction
import cn.edu.ustc.timeflow.bean.TimeRestriction
import org.junit.Test

import org.junit.Assert.*
import java.time.LocalTime

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
}
class ActionTest {
    @Test
    fun testAction() {
        val action = Action(6,"test")
        action.id = 1
    }
    @Test
    fun testRestriction() {
         var list :List<Restriction>  = mutableListOf(
             TimeRestriction("2020-12-12 12:12:12"),
             FixedTimeRestriction("2020-12-12 12:12:12"),
             RepeatRestriction("2020-12-12 12:12:12")
         )
    }
}
class FixedTimeRestrictionTest {

    @Test
    fun initializationWithCode() {
        val restriction = FixedTimeRestriction("08:00 10:00 0 1 2 3")
        assertEquals(LocalTime.of(8, 0), restriction.start)
        assertEquals(LocalTime.of(10, 0), restriction.end)
        assertEquals(0, restriction.type)
        assertEquals(listOf(1, 2, 3), restriction.days)
    }

    @Test
    fun initializationWithParameters() {
        val start = LocalTime.of(8, 0)
        val end = LocalTime.of(10, 0)
        val days = listOf(1, 2, 3)
        val restriction = FixedTimeRestriction(start, end, 0, days)
        assertEquals(start, restriction.start)
        assertEquals(end, restriction.end)
        assertEquals(0, restriction.type)
        assertEquals(days, restriction.days)
    }

    @Test
    fun toStringRepresentation() {
        val restriction = FixedTimeRestriction("08:00 10:00 0 1 2 3")
        assertEquals("FixedTimeRestriction:08:00 10:00 0 1 2 3", restriction.toString())
    }

    @Test
    fun emptyDaysList() {
        val restriction = FixedTimeRestriction("08:00 10:00 0")
        assertTrue(restriction.days.isEmpty())
    }

    @Test
    fun invalidTimeFormat() {
        try {
            FixedTimeRestriction("invalid time format")
            fail("Exception should have been thrown for invalid time format")
        } catch (e: Exception) {
            // Expected exception
        }
    }

    @Test
    fun invalidTypeFormat() {
        try {
            FixedTimeRestriction("08:00 10:00 invalid")
            fail("Exception should have been thrown for invalid type format")
        } catch (e: Exception) {
            // Expected exception
        }
    }

    @Test
    fun invalidDaysFormat() {
        try {
            FixedTimeRestriction("08:00 10:00 0 invalid")
            fail("Exception should have been thrown for invalid days format")
        } catch (e: Exception) {
            // Expected exception
        }
    }
}

