package com.example.timeflow

import android.util.Log
import cn.edu.ustc.timeflow.bean.Action
import cn.edu.ustc.timeflow.restriction.FixedTimeRestriction
import cn.edu.ustc.timeflow.restriction.RepeatRestriction
import cn.edu.ustc.timeflow.restriction.Restriction
import cn.edu.ustc.timeflow.restriction.TimeRestriction
import cn.edu.ustc.timeflow.util.RestrictionFactory
import cn.edu.ustc.timeflow.util.ScheduleConverter
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
        val restriction =
            FixedTimeRestriction("08:00 10:00 0 1 2 3")
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
        val restriction = FixedTimeRestriction(
            start,
            end,
            FixedTimeRestriction.FixedTimeRestrictionType.DAILY,
            days
        )
        assertEquals(start, restriction.start)
        assertEquals(end, restriction.end)
        assertEquals(0, restriction.type)
        assertEquals(days, restriction.days)
    }

    @Test
    fun codingRepresentation() {
        val restriction =
            FixedTimeRestriction("08:00 10:00 0 1 2 3")
        assertEquals("FixedTimeRestriction:08:00 10:00 0 1 2 3", restriction.coding())
    }

    @Test
    fun emptyDaysList() {
        val restriction =
            FixedTimeRestriction("08:00 10:00 0")
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
class ScheduleParseTest {

    @Test
    fun parseSingleWeekCourse() {
        val code = "3周 3C102 :3(8,9) 叶盛"
        val converter = ScheduleConverter(code)
        val items = converter.parse()
        assertEquals(1, items.size)

        val item = items[0]
        assertEquals(listOf(3), item.StartWeeks)
        assertEquals(listOf(3), item.EndWeeks)
        assertEquals("3C102", item.Room)
        assertEquals("3(8,9)", item.Time)
        assertEquals("叶盛", item.Teacher)
        assertEquals(listOf(0), item.EvenOrOddWeeks)
    }

    @Test
    fun parseEvenWeeksCourse() {
        val code = "2~6(双)周 3C102 :3(3,4) 叶盛"
        val converter = ScheduleConverter(code)
        val items = converter.parse()
        assertEquals(1, items.size)

        val item = items[0]
        assertEquals(listOf(2), item.StartWeeks)
        assertEquals(listOf(6), item.EndWeeks)
        assertEquals("3C102", item.Room)
        assertEquals("3(3,4)", item.Time)
        assertEquals("叶盛", item.Teacher)
        assertEquals(listOf(2), item.EvenOrOddWeeks)
    }

    @Test
    fun parseOddWeeksCourse() {
        val code = "1~5(单)周 3C102 :2(1,2) 蔡振翼"
        val converter = ScheduleConverter(code)
        val items = converter.parse()
        assertEquals(1, items.size)

        val item = items[0]
        assertEquals(listOf(1), item.StartWeeks)
        assertEquals(listOf(5), item.EndWeeks)
        assertEquals("3C102", item.Room)
        assertEquals("2(1,2)", item.Time)
        assertEquals("蔡振翼", item.Teacher)
        assertEquals(listOf(1), item.EvenOrOddWeeks)
    }

    @Test
    fun parseMultipleWeekRanges() {
        val code = "2~4,6~18周 3C102 :1(3,4) 叶盛"
        val converter = ScheduleConverter(code)
        val items = converter.parse()
        assertEquals(1, items.size)

        val item = items[0]
        assertEquals(listOf(2, 6), item.StartWeeks)
        assertEquals(listOf(4, 18), item.EndWeeks)
        assertEquals("3C102", item.Room)
        assertEquals("1(3,4)", item.Time)
        assertEquals("叶盛", item.Teacher)
        assertEquals(listOf(0, 0), item.EvenOrOddWeeks)
    }

    @Test
    fun parseComplexCourses() {
        val code = """
            2~4,6~18周 3C102 :1(3,4) 叶盛
            2~6(双),7~18周 3C102 :3(3,4) 叶盛
            3周 3C102 :3(8,9) 叶盛
            2~3,6~18周 3C102 :5(3,4) 叶盛
            5周 3C102 :6(3,4) 叶盛
            6周 3C102 :7(3,4) 叶盛
        """.trimIndent()
        val converter = ScheduleConverter(code)
        val items = converter.parse()
        assertEquals(6, items.size)

        val item1 = items[0]
        assertEquals(listOf(2, 6), item1.StartWeeks)
        assertEquals(listOf(4, 18), item1.EndWeeks)
        assertEquals("3C102", item1.Room)
        assertEquals("1(3,4)", item1.Time)
        assertEquals("叶盛", item1.Teacher)
        assertEquals(listOf(0, 0), item1.EvenOrOddWeeks)

        val item2 = items[1]
        assertEquals(listOf(2, 7), item2.StartWeeks)
        assertEquals(listOf(6, 18), item2.EndWeeks)
        assertEquals("3C102", item2.Room)
        assertEquals("3(3,4)", item2.Time)
        assertEquals("叶盛", item2.Teacher)
        assertEquals(listOf(2, 0), item2.EvenOrOddWeeks)

        val item3 = items[2]
        assertEquals(listOf(3), item3.StartWeeks)
        assertEquals(listOf(3), item3.EndWeeks)
        assertEquals("3C102", item3.Room)
        assertEquals("3(8,9)", item3.Time)
        assertEquals("叶盛", item3.Teacher)
        assertEquals(listOf(0), item3.EvenOrOddWeeks)

        val item4 = items[3]
        assertEquals(listOf(2, 6), item4.StartWeeks)
        assertEquals(listOf(3, 18), item4.EndWeeks)
        assertEquals("3C102", item4.Room)
        assertEquals("5(3,4)", item4.Time)
        assertEquals("叶盛", item4.Teacher)
        assertEquals(listOf(0, 0), item4.EvenOrOddWeeks)

        val item5 = items[4]
        assertEquals(listOf(5), item5.StartWeeks)
        assertEquals(listOf(5), item5.EndWeeks)
        assertEquals("3C102", item5.Room)
        assertEquals("6(3,4)", item5.Time)
        assertEquals("叶盛", item5.Teacher)
        assertEquals(listOf(0), item5.EvenOrOddWeeks)

        val item6 = items[5]
        assertEquals(listOf(6), item6.StartWeeks)
        assertEquals(listOf(6), item6.EndWeeks)
        assertEquals("3C102", item6.Room)
        assertEquals("7(3,4)", item6.Time)
        assertEquals("叶盛", item6.Teacher)
        assertEquals(listOf(0), item6.EvenOrOddWeeks)
    }

    @Test
    fun RestrictionFactoryTest() {
        val restriction2: Restriction = FixedTimeRestriction(LocalTime.of(23,0),LocalTime.of(7,0),FixedTimeRestriction.FixedTimeRestrictionType.DAILY, ArrayList())
        Log.d("RestrictionFactoryTest",restriction2.coding())
        val restriction = RestrictionFactory(restriction2.coding()).create()
        Log.d("RestrictionFactoryTest",restriction.coding())
    }
}