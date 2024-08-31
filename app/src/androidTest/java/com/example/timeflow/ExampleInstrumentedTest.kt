package com.example.timeflow

import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import cn.edu.ustc.timeflow.restriction.FixedTimeRestriction
import cn.edu.ustc.timeflow.restriction.Restriction
import cn.edu.ustc.timeflow.util.RestrictionFactory

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import java.time.LocalTime

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

//    @Test
//    fun testInsert() {
//        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
//        val db = TestDB.getDatabase(appContext)
//        val dao = db.testDao()
//        val testData = TestData()
//        testData.name = "test"
//        testData.age = 20
//        dao.insert(testData)
//
//    }
//    @Test
//    fun testQuery() {
//        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
//        val db = TestDB.getDatabase(appContext)
//        val dao = db.testDao()
//        val testData2 = TestData()
//        testData2.name = "test"
//        testData2.age = 20
//        dao.insert(testData2)
//
//        val testData = dao.getAll()
//        Log.d("DBTest", "testQuery: "+testData.size)
//        for (data in testData) {
//            Log.d("DBTest", data.name+"  "+data.age+ " "+ data.DateTime.toString())
//        }
//    }
//
//    @Test
//    fun testUpdate() {
//        Log.d("Test", "testUpdate")
//    }
@Test
fun RestrictionFactoryTest() {
    val restriction2: Restriction = FixedTimeRestriction(
        LocalTime.of(23,0),
        LocalTime.of(7,0),
        FixedTimeRestriction.FixedTimeRestrictionType.DAILY, ArrayList())
    Log.d("RestrictionFactoryTest",restriction2.coding())
    val restriction = RestrictionFactory(restriction2.coding()).create()
    Log.d("RestrictionFactoryTest",restriction.coding())
}
}