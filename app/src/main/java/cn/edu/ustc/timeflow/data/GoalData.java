package cn.edu.ustc.timeflow.data;

import cn.edu.ustc.timeflow.bean.Goal;

public class GoalData {
    public static Goal getGoalByID(){
        return new Goal();
    }
    public static void addGoal(Goal goal){
    }
    public static void updateGoal(Goal goal){
    }
    public static void deleteGoal(Goal goal){
    }
    public static void deleteGoal(int goalID){
    }
    public static void deleteGoalByActionID(int actionID){
    }
    public static void deleteGoalByTaskID(int taskID){
    }

    public static double getGoalPriorityByActionID(int actionID){
        return 0;
    }

}
