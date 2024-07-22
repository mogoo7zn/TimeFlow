package cn.edu.ustc.timeflow.data;

import java.time.LocalDateTime;
import java.util.List;
import cn.edu.ustc.timeflow.bean.Task;

/**
 * Provides data access methods for tasks.
 * Includes methods for fetching and updating tasks based on time or status.
 */
public class TaskData {
    public static List<Task> getUnfinishedTask(){
        return null;
    }

    public static void addToDoTask(List<Task> tasks){
    }

    public static List<Task> getTaskByTime(LocalDateTime start, LocalDateTime end){
        return List.of();
    }

    public static void updateTask(Task task){
    }
}