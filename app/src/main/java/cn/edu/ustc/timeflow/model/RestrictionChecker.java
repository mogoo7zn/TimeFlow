package cn.edu.ustc.timeflow.model;

import android.content.Context;

import cn.edu.ustc.timeflow.bean.Action;
import cn.edu.ustc.timeflow.bean.Task;
import cn.edu.ustc.timeflow.restriction.Restriction;

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
            if (!restriction.check(task)) {
                return false;
            }
        }
        return true;
    }
}
