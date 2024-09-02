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
    /**
     * 任务完成度
     */
    @ColumnInfo(name = "goal_measure")
    String measure;
    /**
     * 任务优先级
     */
    @ColumnInfo(name = "goal_priority")
    int priority;
    /**
     * 任务是否完成
     */
    @ColumnInfo(name = "goal_finished")
    boolean finished;
    /**
     * 任务是否激活
     */
    @ColumnInfo(name = "goal_active")
    boolean active;

    @Ignore
    List<Milestone> milestones;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Goal() {
    }
    @Ignore
    public Goal(String content, LocalDateTime start, LocalDateTime end, String reason, int priority) {
        this.content = content;
        this.start = start;
        this.end = end;
        this.reason = reason;
        this.priority = priority;
    }
}
