package cn.edu.ustc.timeflow.util

import android.content.Context
import android.util.Log
import cn.edu.ustc.timeflow.bean.Action
import cn.edu.ustc.timeflow.bean.Goal
import cn.edu.ustc.timeflow.bean.Milestone
import cn.edu.ustc.timeflow.bean.Task
import cn.edu.ustc.timeflow.dao.ActionDao
import cn.edu.ustc.timeflow.dao.GoalDao
import cn.edu.ustc.timeflow.dao.MilestoneDao
import cn.edu.ustc.timeflow.dao.TaskDao
import cn.edu.ustc.timeflow.database.ActionDB
import cn.edu.ustc.timeflow.database.GoalDB
import cn.edu.ustc.timeflow.database.MilestoneDB
import cn.edu.ustc.timeflow.database.TaskDB
import cn.edu.ustc.timeflow.restriction.FixedTimeRestriction
import cn.edu.ustc.timeflow.restriction.RepeatRestriction
import cn.edu.ustc.timeflow.restriction.Restriction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import kotlin.random.Random

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

        taskDao.deleteAll()

        val currentDate = LocalDate.now()


        val tasks = generateRandomTasks()

        tasks.forEach { taskDao.insert(it) }


    }

    fun generateRandomTasks(): List<Task> {
        val taskList = mutableListOf<Task>()
        val currentDate = LocalDate.now()
        val random = Random(System.currentTimeMillis())

        for (i in 1..100) {
            val randomDayOffset = random.nextInt(-7, 8) // Random day within two weeks
            val randomStartHour = random.nextInt(0, 24)
            val randomStartMinute = random.nextInt(0, 60)
            val randomDurationMinutes = random.nextInt(30, 600) // Task duration between 30 minutes to 3 hours

            val startDateTime = LocalDateTime.of(currentDate.plusDays(randomDayOffset.toLong()), LocalTime.of(randomStartHour, randomStartMinute))
            val endDateTime = startDateTime.plusMinutes(randomDurationMinutes.toLong())

            val task = Task(
                startDateTime,
                endDateTime,
                "Task $i",
                random.nextInt(1, 5),
                listOf("Excellent", "Good", "Average", "Poor")[random.nextInt(0, 4)],
                random.nextBoolean(),
                random.nextInt(1, 10)
            )

            taskList.add(task)
        }

        return taskList
    }


    fun generateSample(){
        val goalDao = getGoalDao()
        val milestoneDao = getMilestoneDao()
        val actionDao = getActionDao()
        val taskDao = getTaskDao()
        // clear all data
        goalDao.deleteAll()
        milestoneDao.deleteAll()
        actionDao.deleteAll()
        taskDao.deleteAll()
        // Add Goal: live a healthy life
        val health_goal:Goal = Goal()
        health_goal.content = "live a healthy life"
        health_goal.priority = 1
        health_goal.start = null
        health_goal.end = null
        health_goal.isActive = true
        health_goal.isFinished = false
        health_goal.reason = "I want to live longer and happier, and I want to be more energetic and more productive, so that I can achieve more in my life."

        goalDao.insert(health_goal)

        // Add Milestone: lose 10kg in 3 months; sleep on time for 1 month
        val milestone1: Milestone = Milestone()
        milestone1.goal_id = health_goal.id
        milestone1.content = "lose 10kg in 3 months"
        milestone1.time = LocalDateTime.now().plusMonths(3)
        milestone1.isFinished = false

        val milestone2: Milestone = Milestone()
        milestone2.goal_id = health_goal.id
        milestone2.content = "sleep on time for 1 month"
        milestone2.time = LocalDateTime.now().plusMonths(1)
        milestone2.isFinished = false

        milestoneDao.insert(milestone1)
        milestoneDao.insert(milestone2)

        // Add Action: go to gym 3 times a week; sleep from 11:00pm to 7:00am
        val action1: Action = Action()
        action1.goal_id = health_goal.id
        action1.name = "go to gym"
        action1.type = "Repeating"
        action1.duration = Duration.ofHours(1)
        action1.location = "gym"
        action1.note = "remember to bring water, and wear sportswear"
        action1.overlapping = false

        val restriction1: Restriction = RepeatRestriction(-1,-1,7,3)
        action1.addRestriction(restriction1)

        val action2: Action = Action()
        action2.goal_id = health_goal.id
        action2.name = "sleep on time"
        action2.type = "Fixed"
        action2.duration = Duration.ofHours(8)
        action2.location = "bedroom"
        action2.note = "put the phone away!!!"
        action2.overlapping = false

        val restriction2: Restriction = FixedTimeRestriction(LocalTime.of(23,0),LocalTime.of(7,0),FixedTimeRestriction.FixedTimeRestrictionType.DAILY, ArrayList())
        action2.addRestriction(restriction2)

        actionDao.insert(action1)
        actionDao.insert(action2)

    }
}