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

    int importance;

    @ColumnInfo(name = "action_id")
    int action_id;
    @ColumnInfo(name = "evaluation")
    String evaluation;
    @ColumnInfo(name = "finished")
    Boolean finished;

    public Task(Action action, LocalDateTime start, LocalDateTime end){
        //tOdO: implement
    }
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
}
