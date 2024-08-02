package cn.edu.ustc.timeflow.bean;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.time.Duration;
import java.util.List;

import cn.edu.ustc.timeflow.restriction.Restriction;
import cn.edu.ustc.timeflow.util.DurationConverter;
import cn.edu.ustc.timeflow.util.RestrictionConverter;

/**
 * Represents an action associated with a task.
 */
@Entity (tableName = "action")
public class Action {
    @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo(name = "goal_id")
    int goal_id;
    @ColumnInfo(name = "action_name")
    String name;
    @ColumnInfo(name = "action_duration")
    @TypeConverters(DurationConverter.class)
    Duration duration;
    @ColumnInfo(name = "action_location")
    String location;
    @ColumnInfo(name = "action_note")
    String note;
    @ColumnInfo(name = "action_remind")
    boolean remind;
    @ColumnInfo(name = "action_type")
    String type; // Task type: 1. Repeating, 2. Fixed
    @ColumnInfo(name = "action_finished")
    boolean finished;

    @ColumnInfo(name = "action_restrictions")
    @TypeConverters(RestrictionConverter.class)
    List<Restriction> restrictions;


    public Action(int goal_id, String name) {
        this.goal_id = goal_id;
        this.name = name;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getGoal_id() { return goal_id; }
    public void setGoal_id(int goal_id) { this.goal_id = goal_id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Duration getDuration() { return duration; }
    public void setDuration(Duration duration) { this.duration = duration; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public boolean isRemind() { return remind; }
    public void setRemind(boolean remind) { this.remind = remind; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public boolean isFinished() { return finished; }
    public void setFinished(boolean finished) { this.finished = finished; }
    public List<Restriction> getRestrictions() { return restrictions; }
    public void setRestrictions(List<Restriction> restrictions) { this.restrictions = restrictions; }
}