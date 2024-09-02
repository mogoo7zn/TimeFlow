package cn.edu.ustc.timeflow.model

import android.content.Context
import cn.edu.ustc.timeflow.bean.Action
import cn.edu.ustc.timeflow.bean.Task
import cn.edu.ustc.timeflow.util.DBHelper
import cn.edu.ustc.timeflow.util.TimeTable
import com.google.ortools.sat.CpModel
import com.google.ortools.sat.IntervalVar
import java.time.LocalDateTime
import java.time.ZoneOffset


class OrToolsScheduler(context: Context?, valuer: Valuer?) : Scheduler(context, valuer) {
    var model: CpModel = CpModel()

    override fun Schedule(start: LocalDateTime, end: LocalDateTime): TimeTable {
        val timeTable = TimeTable(context, start, end)
        val map = HashMap<Action, IntervalVar>()
        val actions = DBHelper(context).getActionDao().getAll()

        // 添加变量与约束
        for (action in actions){
            map[action] = model.newIntervalVar(model.newIntVar(0, 100, "start"), model.newIntVar(0, 100, "end"), model.newIntVar(0, 100, "duration"), action.name)

            // 添加约束
            for(restriction in action.restrictions){
                //TODO: 添加约束
            }
        }

        // 求解
        val solver = com.google.ortools.sat.CpSolver()
        val status = solver.solve(model)

        // 获取结果
        if (status == com.google.ortools.sat.CpSolverStatus.OPTIMAL || status == com.google.ortools.sat.CpSolverStatus.FEASIBLE) {
            for ((action, interval) in map) {
                val intervalStart = solver.value(interval.startExpr)
                val intervalEnd = solver.value(interval.endExpr)

                val startDateTime = LocalDateTime.ofEpochSecond(intervalStart, 0, ZoneOffset.ofHours(8))
                val endDateTime = LocalDateTime.ofEpochSecond(intervalEnd, 0, ZoneOffset.ofHours(8))
                timeTable.addTask(Task(action, startDateTime, endDateTime))
            }
        } else {
            // Handle the case where no feasible solution was found
            println("No feasible solution found.")
        }




        return timeTable
    }

    override fun Schedule() {
    }
}


