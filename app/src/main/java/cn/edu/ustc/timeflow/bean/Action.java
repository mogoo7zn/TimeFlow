package cn.edu.ustc.timeflow.bean;

import java.time.Duration;
import java.util.List;

/**
 * 任务的具体行动
 */
public class Action {

    int id;
    int goal_id;
    String name;



    Duration duration;
    String location;
    String note;
    boolean remind;
    /**
     * 任务类型
     *  1. 重复任务
     *  2. 固定任务
     */
    String type;


    List<Restriction> restrictions;


    public Action(int goal_id, String name) {
        this.goal_id = goal_id;
        this.name = name;
    }


    public int getGoal_id() {
        return goal_id;
    }
    public int getId() {
        return id;
    }

    public Duration getDuration() {
        return duration;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setDuration(Duration duration) {
        this.duration = duration;
    }
    public String getName() {
        return name;
    }
}
