package cn.edu.ustc.timeflow.model;

import android.content.Context;

import cn.edu.ustc.timeflow.bean.Action;
import cn.edu.ustc.timeflow.bean.Task;
import cn.edu.ustc.timeflow.dao.TaskDao;
import cn.edu.ustc.timeflow.restriction.AmountRestriction;
import cn.edu.ustc.timeflow.restriction.IntervalRestriction;
import cn.edu.ustc.timeflow.restriction.Restriction;
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
        for (Restriction restriction : action.getRestrictions()) {
            if (restriction.getClass().getSimpleName().equals("AmountRestriction")) {
                int total = ((AmountRestriction) restriction).getTotal();
                int todo = ((AmountRestriction) restriction).getTodo();
                int finished = ((AmountRestriction) restriction).getFinished();
                if (total <= todo + finished) {
                    return false;
                }
            }
            else if (restriction.getClass().getSimpleName().equals("IntervalRestriction")) {
                IntervalRestriction intervalRestriction = (IntervalRestriction) restriction;
                TaskDao taskDao = new DBHelper(context).getTaskDao();
                if (taskDao.countByActionIdWithTime(action.getId(), task.getStart().minusDays(intervalRestriction.getInterval()),task.getStart()) >= intervalRestriction.getRepeat_times()) {
                    return false;
                }
            }
        }
        return true;
    }
}
