package cn.edu.ustc.timeflow.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import cn.edu.ustc.timeflow.bean.Milestone
import cn.edu.ustc.timeflow.dao.MilestoneDao

@Database(entities = [Milestone::class], version = 1)
abstract class MilestoneDB : RoomDatabase() {
    abstract fun milestoneDao(): MilestoneDao
    companion object {
        @Volatile
        private var INSTANCE: MilestoneDB? = null
        fun getDatabase(context: Context): MilestoneDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MilestoneDB::class.java,
                    "milestone_database"
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