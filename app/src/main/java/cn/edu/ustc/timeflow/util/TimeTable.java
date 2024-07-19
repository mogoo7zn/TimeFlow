package cn.edu.ustc.timeflow.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import cn.edu.ustc.timeflow.bean.Task;
import cn.edu.ustc.timeflow.data.TaskData;
import kotlin.Pair;

public class TimeTable {
    List<Task> tasks;
    public TimeTable(LocalDateTime start, LocalDateTime end){
        tasks = TaskData.getTaskByTime(start, end);
    }

    public List<Pair<LocalDateTime,LocalDateTime> > getAvailableTime(){
        return List.of();
    }
}
