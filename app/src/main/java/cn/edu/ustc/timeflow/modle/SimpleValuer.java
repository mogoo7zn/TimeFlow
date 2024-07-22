package cn.edu.ustc.timeflow.modle;

import cn.edu.ustc.timeflow.bean.Action;
import cn.edu.ustc.timeflow.data.GoalData;

public class SimpleValuer implements Valuer {
    /**
     * 这是一个简单的估值器，它根据action所属的goal的优先级来估值
     * @param action
     * @return 返回action所属的goal的优先级
     */
    public double valuate(Action action) {
        return GoalData.getGoalPriorityByActionID(action.getId());
    }
}
