package cn.edu.ustc.timeflow.util

import android.content.Context
import android.widget.Toast
import cn.edu.ustc.timeflow.bean.Task
import cn.edu.ustc.timeflow.database.TaskDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.util.Stack

class TimeTable {
    var context: Context? = null
    lateinit var start: LocalDateTime
    lateinit var end: LocalDateTime
    var tasks: MutableList<Task> = mutableListOf()
        get() {
            // 按开始时间排序
            field.sortBy { it.start }
            return field
        }


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

    constructor(context: Context?,Type:Int, date: LocalDate) {
        if(Type==1){//get timetable of this week
            this.context = context
            this.start = date.atStartOfDay()
            this.end = date.atTime(23, 59, 59)
            val taskDao = TaskDB.getDatabase(context!!).taskDao()
            tasks = taskDao.getByTime(start, end).toMutableList()
            checkOverlap()
        }
        else if(Type==2){//get timetable of this month
            this.context = context
            this.start = date.atStartOfDay()
            this.end = date.atTime(23, 59, 59)
            val taskDao = TaskDB.getDatabase(context!!).taskDao()
            tasks = taskDao.getByTime(start, end).toMutableList()
            checkOverlap()
        }
        else if(Type==3){//get timetable of this year
            this.context = context
            this.start = date.atStartOfDay()
            this.end = date.atTime(23, 59, 59)
            val taskDao = TaskDB.getDatabase(context!!).taskDao()
            tasks = taskDao.getByTime(start, end).toMutableList()
            checkOverlap()

        }
    }

    fun addTask(task: Task) {
        tasks.add(task)
    }

    fun addTasks(tasks: List<Task>) {
        this.tasks.addAll(tasks)
    }

    fun sync() {
        sync(false)
    }

    fun sync(usingCoroutine:Boolean){
        if (!usingCoroutine){
            val taskDao = TaskDB.getDatabase(context!!).taskDao()
            taskDao.deleteByTime(start, end)
            taskDao.insert(tasks)
        }
        else{
            CoroutineScope(Dispatchers.IO).launch {
                val taskDao = TaskDB.getDatabase(context!!).taskDao()
                taskDao.deleteByTime(start, end)
                taskDao.insert(tasks)
            }
        }


    }


    fun getAvailableTime(): List<Pair<LocalDateTime, LocalDateTime>> {
        val availableTime: MutableList<Pair<LocalDateTime, LocalDateTime>> = mutableListOf()
        var currentStart = start

        if (tasks.isEmpty()) {
            availableTime.add(Pair(start, end))
            return availableTime
        }

        for (task in tasks) {
            if (currentStart < task.start) {
                availableTime.add(Pair(currentStart, task.start))
            }
            // 更新currentStart为 task.end 与 currentStart 中较迟的一个
            currentStart = if (task.end > currentStart) task.end else currentStart
        }

        if (currentStart < end) {
            availableTime.add(Pair(currentStart, end))
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
        //按照任务时长排序，时长越长的任务越靠前（在底层）
        tasks.sortBy {- it.end.toEpochSecond(ZoneOffset.UTC) + it.start.toEpochSecond(ZoneOffset.UTC) }
        //检查是否有重叠的任务，如果有则将task.overlap设为重叠了几次（即同时有几个任务），并更新重叠序号task.overlapIndex (表示同时有几个任务中的第几个)
        for (i in tasks.indices) {
            tasks[i].overlap = 0

            for(j in 0..i-1){
                if(tasks[j].start < tasks[i].end && tasks[j].end > tasks[i].start){
                    tasks[i].overlap++
                }
            }
        }
    }

    fun changeTaskTime(taskId: Int, localTime: LocalTime) {
        val task = tasks.find { it.id == taskId }
        if (task != null) {
            val duration = Duration.between(task.start, task.end)
            val start = task.start.toLocalDate().atTime(localTime)
            val end = start.plus(duration)
            task.start = start
            task.end = end
            sync()
        }
    }

    fun changeTaskFinished(taskId: Int, checked: Boolean) {
        val task = tasks.find { it.id == taskId }
        if (task != null) {
            task.finished = checked
            sync()
        }
    }

}