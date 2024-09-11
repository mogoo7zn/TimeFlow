package cn.edu.ustc.timeflow.model;

import android.content.Context;

import java.time.LocalDateTime;
import java.util.List;

import cn.edu.ustc.timeflow.bean.Action;
import cn.edu.ustc.timeflow.bean.Task;
import cn.edu.ustc.timeflow.dao.TaskDao;
import cn.edu.ustc.timeflow.restriction.AmountRestriction;
import cn.edu.ustc.timeflow.restriction.IntervalRestriction;
import cn.edu.ustc.timeflow.restriction.Restriction;
import cn.edu.ustc.timeflow.restriction.TimeRestriction;
import cn.edu.ustc.timeflow.util.DBHelper;

public class RestrictionChecker {
    Context context;
    Action action;
    Task task;

    public RestrictionChecker(Context context, Action action, Task task) {
        this.context = context;
        this.action =  action;
        this.task = task;
    }

    public boolean RestrictionCheck() {
        if(task.getEnd().isBefore(LocalDateTime.now()))
            return false;


        for (Restriction restriction : action.getRestrictions("AmountRestriction")) {
            AmountRestriction amountRestriction = (AmountRestriction) restriction;
            int total = amountRestriction.getTotal();
            int todo = amountRestriction.getTodo();
            int finished = amountRestriction.getFinished();
            if (total <= todo + finished) {
                return false;
            }
        }


        for (Restriction restriction : action.getRestrictions("IntervalRestriction")) {
            IntervalRestriction intervalRestriction = (IntervalRestriction) restriction;
            TaskDao taskDao = new DBHelper(context).getTaskDao();
            if (taskDao.countByActionIdWithTime(action.getId(), task.getStart().minusDays(intervalRestriction.getInterval()),task.getStart()) >= intervalRestriction.getRepeat_times()) {
                return false;
            }
        }

        boolean tag = action.getRestrictions("TimeRestriction").isEmpty();

        for (Restriction restriction : action.getRestrictions("TimeRestriction")) {
            // 其中有一个符合即可
            TimeRestriction timeRestriction = (TimeRestriction) restriction;
            if (task.getStart().isAfter(timeRestriction.getStart()) && task.getStart().isBefore(timeRestriction.getEnd())) {
                tag = true;
                break;
            }
        }
        if (!tag) {
            return false;
        }
        return true;
    }
}
