package cn.edu.ustc.timeflow.util

import android.content.Context
import android.util.Log
import cn.edu.ustc.timeflow.bean.Action
import cn.edu.ustc.timeflow.bean.Task
import cn.edu.ustc.timeflow.dao.ActionDao
import cn.edu.ustc.timeflow.dao.GoalDao
import cn.edu.ustc.timeflow.dao.MilestoneDao
import cn.edu.ustc.timeflow.dao.TaskDao
import cn.edu.ustc.timeflow.database.ActionDB
import cn.edu.ustc.timeflow.database.GoalDB
import cn.edu.ustc.timeflow.database.MilestoneDB
import cn.edu.ustc.timeflow.database.TaskDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime

class DBHelper (val context :Context){
    fun getActionDao() : ActionDao {
        return ActionDB.getDatabase(context).actionDao()
    }
    fun getGoalDao() : GoalDao {
        return GoalDB.getDatabase(context).goalDao()
    }
    fun getMilestoneDao() : MilestoneDao {
        return MilestoneDB.getDatabase(context).milestoneDao()
    }
    fun getTaskDao() : TaskDao {
        return TaskDB.getDatabase(context).taskDao()
    }

    fun close(){
        ActionDB.getDatabase(context).close()
        GoalDB.getDatabase(context).close()
        MilestoneDB.getDatabase(context).close()
        TaskDB.getDatabase(context).close()
    }

    fun clear(){
        ActionDB.getDatabase(context).clearAllTables()
        GoalDB.getDatabase(context).clearAllTables()
        MilestoneDB.getDatabase(context).clearAllTables()
        TaskDB.getDatabase(context).clearAllTables()
    }

    fun generateTestTaskData() {

        val taskDao = getTaskDao()
        val tasks = listOf(
            Task(LocalDateTime.of(2024, 8, 18, 9, 0), LocalDateTime.of(2024, 8, 18, 10, 0), "Task 1", 1, "Good", false, 3),
            Task(LocalDateTime.of(2024, 8, 18, 11, 0), LocalDateTime.of(2024, 8, 18, 12, 0), "Task 2", 1, "Average", false, 2),
            Task(LocalDateTime.of(2024, 8, 18, 9, 0), LocalDateTime.of(2024, 8, 18, 10, 0), "Task 3", 2, "Excellent", true, 5),
            Task(LocalDateTime.of(2024, 8, 18, 11, 0), LocalDateTime.of(2024, 8, 18,12, 0), "Task 4", 2, "Poor", true, 1)
        )

        // Insert tasks into the database
        for (task in tasks) {
            taskDao.insert(task)
        }
        close()

    }

    fun generateTestTaskData(isdb: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            val taskDao = getTaskDao()

            taskDao.deleteAll()

            val currentDate = LocalDate.now()


            val tasks = listOf(
                Task(LocalDateTime.of(currentDate.year, currentDate.month, currentDate.dayOfMonth, 9, 0), LocalDateTime.of(currentDate.year, currentDate.month, currentDate.dayOfMonth, 10, 0), "Task 1", 1, "Good", false, 3),
                Task(LocalDateTime.of(currentDate.year, currentDate.month, currentDate.dayOfMonth, 11, 0), LocalDateTime.of(currentDate.year, currentDate.month, currentDate.dayOfMonth, 12, 0), "Task 2", 1, "Average", false, 2),
                Task(LocalDateTime.of(currentDate.year, currentDate.month, currentDate.dayOfMonth, 9, 0), LocalDateTime.of(currentDate.year, currentDate.month, currentDate.dayOfMonth, 10, 0), "Task 3", 2, "Excellent", true, 5),
                Task(LocalDateTime.of(currentDate.year, currentDate.month, currentDate.dayOfMonth, 11, 0), LocalDateTime.of(currentDate.year, currentDate.month, currentDate.dayOfMonth, 12, 0), "Task 4", 2, "Poor", true, 1)
            )

            // Insert tasks into the database
            tasks.forEach { taskDao.insert(it) }
            taskDao.getAll().forEach { Log.d("Task", it.toString()) }
        }
    }

}