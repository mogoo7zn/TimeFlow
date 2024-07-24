package com.example.timeflow

import cn.edu.ustc.timeflow.bean.Action
import cn.edu.ustc.timeflow.bean.FixedTimeRestriction
import cn.edu.ustc.timeflow.bean.RepeatRestriction
import cn.edu.ustc.timeflow.bean.Restriction
import cn.edu.ustc.timeflow.bean.TimeRestriction
import org.junit.Test

import org.junit.Assert.*

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