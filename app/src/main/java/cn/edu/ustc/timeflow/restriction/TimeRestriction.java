package cn.edu.ustc.timeflow.restriction;

import androidx.annotation.NonNull;

import java.time.LocalDateTime;

import cn.edu.ustc.timeflow.bean.Task;

public class TimeRestriction extends Restriction{
    LocalDateTime start;
    LocalDateTime end;

    public TimeRestriction(String code){
        String[] codes=code.split(" ");
        start=LocalDateTime.parse(codes[0]);
        end=LocalDateTime.parse(codes[1]);
    }
    public TimeRestriction(LocalDateTime start, LocalDateTime end){
        this.start=start;
        this.end=end;
    }
    @NonNull
    @Override
    public String coding(){
        return "TimeRestriction="+start.toString()+" "+end.toString();
    }
    @Override
    public boolean check(Task task) {
        return start.isBefore(task.getStart()) && end.isAfter(task.getEnd());
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

}
