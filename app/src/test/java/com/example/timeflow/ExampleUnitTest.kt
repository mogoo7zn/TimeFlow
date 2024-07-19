package com.example.timeflow

import cn.edu.ustc.timeflow.bean.Action
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
        val action = Action(23,"s")


    }
}