package cn.edu.ustc.timeflow.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import cn.edu.ustc.timeflow.bean.Goal
import cn.edu.ustc.timeflow.dao.GoalDao

@Database(entities = [Goal::class], version = 1)
abstract class GoalDB : RoomDatabase() {
    abstract fun goalDao(): GoalDao
    companion object {
        @Volatile
        private var INSTANCE: GoalDB? = null
        fun getDatabase(context: Context): GoalDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GoalDB::class.java,
                    "goal_database"
                )
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}