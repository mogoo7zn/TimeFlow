package cn.edu.ustc.timeflow.util;

import android.annotation.SuppressLint;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import cn.edu.ustc.timeflow.bean.Task;
import cn.edu.ustc.timeflow.data.TaskData;
import kotlin.Pair;

public class TimeTable {
    LocalDateTime start;
    LocalDateTime end;
    List<Task> tasks;


    public TimeTable(@NonNull LocalDate date){
        this(date.atStartOfDay(), date.atStartOfDay().plusDays(1));
    }
    public TimeTable(LocalDateTime start, LocalDateTime end){
        tasks = TaskData.getTaskByTime(start, end);
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
    public List<Pair<LocalDateTime,LocalDateTime> > getAvailableTime(){
        return List.of();
    }
}
