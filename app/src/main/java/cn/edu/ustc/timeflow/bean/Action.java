package cn.edu.ustc.timeflow.bean;

import java.time.Duration;
import java.util.List;

/**
 * Represents an action associated with a task.
 */
public class Action {
    int id;
    int goal_id;
    String name;
    Duration duration;
    String location;
    String note;
    boolean remind;
    String type; // Task type: 1. Repeating, 2. Fixed
    boolean finished;
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