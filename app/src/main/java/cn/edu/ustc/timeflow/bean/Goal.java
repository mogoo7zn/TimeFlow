package cn.edu.ustc.timeflow.bean;

import java.util.List;

public class Goal {
    /**
     * 任务id
     */
    int id;
    /**
     * 任务名称
     */
    String content;
    /**
     * 任务时间
     */
    String time;
    /**
     * 任务目的
     */
    String reason;

    String measure;

    List<Milestone> milestones;
    List<Action> actions;

}
