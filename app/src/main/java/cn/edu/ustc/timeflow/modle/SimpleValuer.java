package cn.edu.ustc.timeflow.modle;

import cn.edu.ustc.timeflow.bean.Action;
import cn.edu.ustc.timeflow.data.GoalData;

public class SimpleValuer implements Valuer {
    public double value(Action action) {
        return GoalData.getGoalPriorityByActionID(action.getId());
    }
}
