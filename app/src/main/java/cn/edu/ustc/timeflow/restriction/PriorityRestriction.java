package cn.edu.ustc.timeflow.restriction;

import cn.edu.ustc.timeflow.bean.Task;

public class PriorityRestriction extends Restriction{
    int priority;
    public PriorityRestriction(String code){
        priority=Integer.parseInt(code);
    }
    public PriorityRestriction(int priority){
        this.priority=priority;
    }
    @Override
    public String coding() {
        return "PriorityRestriction="+priority;
    }

    @Override
    public boolean check(Task task) {
        return true;
    }
}
