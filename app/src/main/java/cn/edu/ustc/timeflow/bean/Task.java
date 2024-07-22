package cn.edu.ustc.timeflow.bean;

import java.time.LocalDateTime;

public class Task {
    int id;
    LocalDateTime start;
    LocalDateTime end;
    String content;
    int action_id;
    String evaluation;
    Boolean is_finished;
    public Task(Action action, LocalDateTime start, LocalDateTime end){
        //tOdO: implement
    }
}
