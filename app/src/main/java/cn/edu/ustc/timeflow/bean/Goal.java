package cn.edu.ustc.timeflow.bean;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.time.LocalDateTime;
import java.util.List;

import cn.edu.ustc.timeflow.util.DateTimeConverter;

@Entity
public class Goal {
    /**
     * 任务id
     */
    @PrimaryKey(autoGenerate = true)
    int id;
    /**
     * 任务名称
     */
    @ColumnInfo(name = "goal_name")
    String content;
    /**
     * 任务开始时间
     */
    @ColumnInfo(name = "goal_start")
    @TypeConverters(DateTimeConverter.class)
    LocalDateTime start;
    /**
     * 任务结束时间
     */
    @TypeConverters(DateTimeConverter.class)
    @ColumnInfo(name = "goal_end")
    LocalDateTime end;
    /**
     * 任务目的
     */
    @ColumnInfo(name = "goal_reason")
    String reason;
    @ColumnInfo(name = "goal_measure")
    String measure;
    @ColumnInfo(name = "goal_priority")
    int priority;
    @ColumnInfo(name = "goal_milestones")
    List<Milestone> milestones;

}
