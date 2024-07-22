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
    public static Task getTaskByID(int id){
        return null;
    }
    public static void addTask(Task task){
    }
    public static void deleteTask(Task task){
    }
    public static void deleteTask(int taskID){
    }
    public static void deleteTaskByActionID(int actionID){
    }
    public static void deleteTaskByGoalID(int goalID){
    }
    public static void updateTask(Task task){
    }
    public static void updateTaskStatus(int taskID, boolean status){
    }
}