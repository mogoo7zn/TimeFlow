package cn.edu.ustc.timeflow.data;

import java.time.LocalDateTime;
import java.util.List;

import cn.edu.ustc.timeflow.bean.Task;

public class TaskData {
    public List<Task> getUnfinishedTask(){
        return null;
    }
    public void addToDoTask(List<Task> tasks){
    }

    public static List<Task> getTaskByTime(LocalDateTime start,LocalDateTime end){
        return List.of();
    }
}
