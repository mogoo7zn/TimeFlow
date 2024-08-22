package cn.edu.ustc.timeflow.util

import android.content.Context
import android.util.Log
import android.widget.Toast
import cn.edu.ustc.timeflow.bean.Task
import cn.edu.ustc.timeflow.database.TaskDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalDateTime

class TimeTable {
    var context: Context? = null
    var start: LocalDateTime
    var end: LocalDateTime
    var tasks: MutableList<Task> = mutableListOf()

    constructor(context: Context?, date: LocalDate) {
        this.context = context
        this.start = date.atStartOfDay()
        this.end = date.atTime(23, 59, 59)

        val taskDao = TaskDB.getDatabase(context!!).taskDao()
         tasks = taskDao.getByTime(start, end).toMutableList()

        checkOverlap()
    }

    constructor(context: Context?, start: LocalDateTime, end: LocalDateTime) {
        this.context = context
        this.start = start
        this.end = end
        CoroutineScope(Dispatchers.IO).launch {
            val taskDao = TaskDB.getDatabase(context!!).taskDao()
            tasks = taskDao.getByTime(start, end).toMutableList()
        }

        checkOverlap()
    }

    fun addTask(task: Task) {
        tasks.add(task)
    }

    fun addTasks(tasks: List<Task>) {
        this.tasks.addAll(tasks)
    }

    fun sync() {
        //TODO: sync with database
        CoroutineScope(Dispatchers.IO).launch {
            val taskDao = TaskDB.getDatabase(context!!).taskDao()
            taskDao.deleteByTime(start, end)
            taskDao.insert(tasks)
        }
    }

    fun getAvailableTime(): List<Pair<LocalDateTime, LocalDateTime>> {
        val availableTime: MutableList<Pair<LocalDateTime, LocalDateTime>> = mutableListOf()
        if (tasks.isEmpty()) {
            availableTime.add(Pair(start, end))
            return availableTime
        }
        if (tasks[0].start > start) {
            availableTime.add(Pair(start, tasks[0].start))
        }
        for (i in 0 until tasks.size - 1) {
            if (tasks[i].end < tasks[i + 1].start) {
                availableTime.add(Pair(tasks[i].end, tasks[i + 1].start))
            }
        }
        if (tasks[tasks.size - 1].end < end) {
            availableTime.add(Pair(tasks[tasks.size - 1].end, end))
        }
        return availableTime
    }

    fun deleteTask(task_id: Int) {
        tasks.removeIf { it.id == task_id }
        sync()

    }

    fun checkOverlap(task: Task): Boolean {
        for (t in tasks) {
            if (task.start < t.end && task.end > t.start) {
                return true
            }
        }
        return false
    }

    fun checkOverlap(){
        //检查是否有重叠的任务，如果有则将task.overlap设为重叠了几次（即同时有几个任务），并更新重叠序号task.overlapIndex (表示同时有几个任务中的第几个)
        for (i in 0 until tasks.size) {
            var overlap = 1
            for (j in 0 until tasks.size) {
                if (i != j && tasks[i].start < tasks[j].end && tasks[i].end > tasks[j].start) {
                    overlap++
                }
            }
            tasks[i].overlap = overlap
            tasks[i].overlapIndex = 0
            for (j in 0 until tasks.size) {
                if (i != j && tasks[i].start < tasks[j].end && tasks[i].end > tasks[j].start) {
                    if (tasks[j].start < tasks[i].start) {
                        tasks[i].overlapIndex++
                    } else if (tasks[j].start == tasks[i].start) {
                        if (tasks[j].end < tasks[i].end) {
                            tasks[i].overlapIndex++
                        }
                    }
                }
            }
        }


    }
}