package cn.edu.ustc.timeflow.database

import android.content.Context
import androidx.room.Database
import androidx.room.DatabaseConfiguration
import androidx.room.InvalidationTracker
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
import cn.edu.ustc.timeflow.bean.Action
import cn.edu.ustc.timeflow.dao.ActionDao

@Database(entities = [Action::class], version = 2)
abstract class ActionDB : RoomDatabase() {
    abstract fun actionDao(): ActionDao
    companion object {
        @Volatile
        private var INSTANCE: ActionDB? = null
        fun getDatabase(context: Context): ActionDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ActionDB::class.java,
                    "action_database"
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