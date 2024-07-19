package cn.edu.ustc.timeflow.data;

import cn.edu.ustc.timeflow.bean.Goal;

public class GoalData {
    public Goal getGoalByID(){
        return new Goal();
    }
    public static double getGoalPriorityByActionID(int actionID){
        return 0;
    }

}
