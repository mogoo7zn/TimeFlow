package cn.edu.ustc.timeflow.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import cn.edu.ustc.timeflow.bean.Task
import cn.edu.ustc.timeflow.dao.TaskDao

@Database(entities = [Task::class], version = 1)
abstract class TaskDB : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    companion object {
        @Volatile
        private var INSTANCE: TaskDB? = null
        fun getDatabase(context: Context): TaskDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TaskDB::class.java,
                    "task_database"
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