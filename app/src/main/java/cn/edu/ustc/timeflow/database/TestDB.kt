package cn.edu.ustc.timeflow.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import cn.edu.ustc.timeflow.dao.TestDao
import cn.edu.ustc.timeflow.entity.TestData

@Database(entities = [TestData::class], version = 1)
abstract class TestDB : RoomDatabase() {
    abstract fun testDao(): TestDao
    companion object{
        @Volatile
        private var INSTANCE: TestDB? = null
        fun getDatabase(context: Context): TestDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TestDB::class.java,
                    "test_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }

}