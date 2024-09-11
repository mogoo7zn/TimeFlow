package cn.edu.ustc.timeflow.model;

import android.content.Context;

import java.time.LocalDateTime;

import cn.edu.ustc.timeflow.bean.Action;
import cn.edu.ustc.timeflow.database.GoalDB;
import cn.edu.ustc.timeflow.util.DBHelper;

public class SimpleValuer implements Valuer {
    /**
     * 这是一个简单的估值器，它根据action所属的goal的优先级来估值
     * @param action
     * @return 返回action所属的goal的优先级
     */
    Context context;
    public SimpleValuer(Context context) {
        this.context = context;
    }

    @Override
    public double valuate(Action action, LocalDateTime time) {
        return new DBHelper(context).getGoalDao().getById(action.getGoal_id()).getPriority();
    }
}
