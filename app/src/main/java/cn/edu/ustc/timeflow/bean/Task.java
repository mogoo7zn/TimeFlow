package cn.edu.ustc.timeflow.bean;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.time.LocalDateTime;

import cn.edu.ustc.timeflow.converter.DateTimeConverter;

@Entity(tableName = "task")
public class  Task {
    /**
     * Task id
     */
    @PrimaryKey(autoGenerate = true)
    int id;
    /**
     * Task start time
     */
    @ColumnInfo(name = "task_start")
    @TypeConverters(DateTimeConverter.class)
    LocalDateTime start;
    /**
     * Task end time
     */
    @ColumnInfo(name = "task_end")
    @TypeConverters(DateTimeConverter.class)
    LocalDateTime end;
    /**
     * Task content
     */
    @ColumnInfo(name = "task_content")
    String content;
    /**
     * Task importance
     */
    @ColumnInfo(name = "task_importance")
    int importance;
    /**
     * Task action id
     */
    @ColumnInfo(name = "action_id")
    int action_id;
    /**
     * Task evaluation
     */
    @ColumnInfo(name = "evaluation")
    String evaluation;
    /**
     * Task finished
     */
    @ColumnInfo(name = "finished")
    Boolean finished;

    @ColumnInfo(name = "location")
    String location;

    @ColumnInfo(name = "note")
    String note;

    @ColumnInfo(name = "remind")
    Boolean remind;

    @Ignore
    Integer overlap;


    @Ignore
    public Task(Action action, LocalDateTime start, LocalDateTime end){
        this.start = start;
        this.end = end;
        this.content = action.getName();
        this.importance = 0;
        this.action_id = action.getId();
        this.evaluation = "";
        this.finished = false;
        this.remind = action.isRemind();
        this.location = action.getLocation();
        this.note = action.getNote();
    }
    @Ignore
    public Task(LocalDateTime start, LocalDateTime end, String content, int importance,String evaluation, Boolean finished, int action_id ) {
        this.start = start;
        this.end = end;
        this.content = content;
        this.importance = importance;
        this.action_id = action_id;
        this.evaluation = evaluation;
        this.finished = finished;
    }

    public Task(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getAction_id() {
        return action_id;
    }

    public void setAction_id(int action_id) {
        this.action_id = action_id;
    }

    public String getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(String evaluation) {
        this.evaluation = evaluation;
    }

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }

    public int getImportance() {
        return importance;
    }

    public void setImportance(int importance) {
        this.importance = importance;
    }

    public Integer getOverlap() {
        return overlap;
    }

    public void setOverlap(Integer overlap) {
        this.overlap = overlap;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Boolean getRemind() {
        return remind;
    }

    public void setRemind(Boolean remind) {
        this.remind = remind;
    }
}
