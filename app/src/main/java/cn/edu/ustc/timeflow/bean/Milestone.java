package cn.edu.ustc.timeflow.bean;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.time.LocalDateTime;

import cn.edu.ustc.timeflow.converter.DateTimeConverter;

@Entity(tableName = "milestone")
public class Milestone {
    /**
     * 里程碑id
     */
    @PrimaryKey(autoGenerate = true)
    int id;
    /**
     * 里程碑所属任务id
     */
    @ColumnInfo(name = "goal_id")
    int goal_id;
    /**
     * 里程碑名称
     */
    @ColumnInfo(name = "milestone_name")
    String content;
    /**
     * 里程碑时间
     */
    @ColumnInfo(name = "milestone_time")
    @TypeConverters(DateTimeConverter.class)
    LocalDateTime time;

    @ColumnInfo(name = "milestone_finished")
    boolean finished;

    public String getGoalName() {
        return goalName;
    }

    public void setGoalName(String goalName) {
        this.goalName = goalName;
    }

    @ColumnInfo(name = "goal_name")
    String goalName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGoal_id() {
        return goal_id;
    }

    public void setGoal_id(int goal_id) {
        this.goal_id = goal_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }
}
