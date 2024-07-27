package cn.edu.ustc.timeflow.util;

import android.content.Context;

import androidx.annotation.NonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import cn.edu.ustc.timeflow.bean.Task;
import cn.edu.ustc.timeflow.dao.TaskDao;
import cn.edu.ustc.timeflow.database.TaskDB;
import kotlin.Pair;

/**
 * Manages a collection of tasks within a specified time frame.
 * Provides functionality to add tasks and retrieve available time slots.
 */
public class TimeTable {
    Context context;
    LocalDateTime start;
    LocalDateTime end;
    List<Task> tasks;

    public TimeTable(Context context, @NonNull LocalDate date){
        this.context = context;
        TaskDao taskDao = TaskDB.Companion.getDatabase(context).taskDao();
        tasks = taskDao.getByTime(date.atStartOfDay(), date.atTime(23, 59, 59));
        this.start = date.atStartOfDay();
        this.end = date.atTime(23, 59, 59);
    }

    public TimeTable(Context context, LocalDateTime start, LocalDateTime end){
        TaskDao taskDao = TaskDB.Companion.getDatabase(context).taskDao();
        tasks = taskDao.getByTime(start, end);
        this.start = start;
        this.end = end;
    }

    public void AddTask(Task task){
        tasks.add(task);
    }

    public void AddTask(List<Task> tasks){
        this.tasks.addAll(tasks);
    }

    public List<Task> getTasks(){
        return tasks;
    }

    public List<Pair<LocalDateTime, LocalDateTime>> getAvailableTime(){
        return null;
    }

    public void sync(){
        TaskDao taskDao = TaskDB.Companion.getDatabase(context).taskDao();

        for (Task task : tasks) {
            taskDao.insert(task);
        }
        tasks = taskDao.getByTime(start, end);
    }
}