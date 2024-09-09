package cn.edu.ustc.timeflow.bean;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.io.Serializable;
import java.time.Duration;
import java.util.List;

import cn.edu.ustc.timeflow.restriction.Restriction;
import cn.edu.ustc.timeflow.converter.DurationConverter;
import cn.edu.ustc.timeflow.converter.RestrictionConverter;

/**
 * Represents an action associated with a task.
 */
@Entity (tableName = "action")
public class Action implements Serializable {
    @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo(name = "goal_id")
    int goal_id;
    @ColumnInfo(name = "action_name")
    String name;
    // 不能为null！！！
    @ColumnInfo(name = "action_duration")
    @TypeConverters(DurationConverter.class)
    Duration duration;
    @ColumnInfo(name = "action_location")
    String location;
    @ColumnInfo(name = "action_note")
    String note;    // 行动备注
    @ColumnInfo(name = "action_remind")
    boolean remind;
    /**
     * Task type: 1. Repeating, 2. Fixed
     */
    @ColumnInfo(name = "action_type")
    String type;

    @ColumnInfo(name = "action_finished")
    boolean finished;
    @ColumnInfo(name = "action_restrictions")
    @TypeConverters(RestrictionConverter.class)
    List<Restriction> restrictions;
    @ColumnInfo(name = "action_overlapping")
    Boolean overlapping=false;



    public Action() {
        // Default constructor required for calls to DataSnapshot.getValue(Action.class)
    }
    @Ignore
    public Action(int goal_id, String name) {
        this.goal_id = goal_id;
        this.name = name;
    }
    @Ignore
    public Action(int id, int goal_id, String name, Duration duration, String location, String note, boolean remind, String type, boolean finished, List<Restriction> restrictions) {
        this.id = id;
        this.goal_id = goal_id;
        this.name = name;
        this.duration = duration;
        this.location = location;
        this.note = note;
        this.remind = remind;
        this.type = type;
        this.finished = finished;
        this.restrictions = restrictions;
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
    public void addRestriction(Restriction restriction) {
        if (restrictions == null) restrictions = new java.util.ArrayList<>();
        restrictions.add(restriction);
    }
    public Boolean getOverlapping() {
        return overlapping;
    }
    public void setOverlapping(Boolean overlapping) {
        this.overlapping = overlapping;
    }


    @Override
    public String toString() {
        return "Action{" +
                "id=" + id +
                ", goal_id=" + goal_id +
                ", name='" + name + '\'' +
                ", duration=" + duration +
                ", location='" + location + '\'' +
                ", note='" + note + '\'' +
                ", remind=" + remind +
                ", type='" + type + '\'' +
                ", finished=" + finished +
                ", restrictions=" + new  RestrictionConverter().toString(restrictions) +
                '}';
    }

    public Restriction getRestriction(String type) {
        for (Restriction restriction : restrictions) {
            if (restriction.getClass().getSimpleName().equals(type)) {
                return restriction;
            }
        }
        return null;
    }
    public List<Restriction> getRestrictions(String type) {
        List<Restriction> res = new java.util.ArrayList<>();
        for (Restriction restriction : restrictions) {
            if (restriction.getClass().getSimpleName().equals(type)) {
                res.add(restriction);
            }
        }
        return res;
    }

}