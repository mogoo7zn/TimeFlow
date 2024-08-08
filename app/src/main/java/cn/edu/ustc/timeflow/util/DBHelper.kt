package cn.edu.ustc.timeflow.util

import android.content.Context
import cn.edu.ustc.timeflow.dao.ActionDao
import cn.edu.ustc.timeflow.dao.GoalDao
import cn.edu.ustc.timeflow.dao.MilestoneDao
import cn.edu.ustc.timeflow.dao.TaskDao
import cn.edu.ustc.timeflow.database.ActionDB
import cn.edu.ustc.timeflow.database.GoalDB
import cn.edu.ustc.timeflow.database.MilestoneDB
import cn.edu.ustc.timeflow.database.TaskDB

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
}