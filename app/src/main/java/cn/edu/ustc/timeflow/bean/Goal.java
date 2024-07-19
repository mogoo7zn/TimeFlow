package cn.edu.ustc.timeflow.bean;

import java.time.LocalDateTime;
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

    LocalDateTime start;
    LocalDateTime end;
    /**
     * 任务目的
     */
    String reason;

    String measure;

    int priority;
    List<Milestone> milestones;
    List<Action> actions;

}
