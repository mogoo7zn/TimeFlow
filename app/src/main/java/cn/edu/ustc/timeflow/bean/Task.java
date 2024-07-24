package cn.edu.ustc.timeflow.bean;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.time.LocalDateTime;

import cn.edu.ustc.timeflow.util.DateTimeConverter;

@Entity(tableName = "task")
public class Task {
    @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo(name = "task_start")
    @TypeConverters(DateTimeConverter.class)
    LocalDateTime start;
    @ColumnInfo(name = "task_end")
    @TypeConverters(DateTimeConverter.class)
    LocalDateTime end;
    @ColumnInfo(name = "task_content")
    String content;

    @ColumnInfo(name = "action_id")
    int action_id;
    @ColumnInfo(name = "evaluation")
    String evaluation;
    @ColumnInfo(name = "finished")
    Boolean finished;

    public Task(Action action, LocalDateTime start, LocalDateTime end){
        //tOdO: implement
    }
}
