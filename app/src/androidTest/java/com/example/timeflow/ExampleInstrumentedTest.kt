package com.example.timeflow

import android.util.Log
import android.widget.Toast
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import cn.edu.ustc.timeflow.database.TestDB
import cn.edu.ustc.timeflow.entity.TestData

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.timeflow", appContext.packageName)

    }

    @Test
    fun testInsert() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val db = TestDB.getDatabase(appContext)
        val dao = db.testDao()
        val testData = TestData()
        testData.name = "test"
        testData.age = 20
        dao.insert(testData)

    }
    @Test
    fun testQuery() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val db = TestDB.getDatabase(appContext)
        val dao = db.testDao()
        val testData2 = TestData()
        testData2.name = "test"
        testData2.age = 20
        dao.insert(testData2)

        val testData = dao.getAll()
        Log.d("DBTest", "testQuery: "+testData.size)
        for (data in testData) {
            //Toast.makeText(appContext, data.name+"  "+data.age, Toast.LENGTH_SHORT).show()
            Log.d("DBTest", data.name+"  "+data.age)
        }
    }

    @Test
    fun testUpdate() {
        Log.d("Test", "testUpdate")
    }
}