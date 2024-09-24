package cn.edu.ustc.timeflow.restriction;

import android.util.Log;

import androidx.annotation.NonNull;

import java.time.LocalTime;
import java.util.List;

import cn.edu.ustc.timeflow.bean.Task;

/**
 * 固定时间约束，包括开始时间和结束时间、类型和日期
 * 要求任务按照重复类型和日期，在开始时间和结束时间之间进行
 */
public class FixedTimeRestriction extends Restriction{
    LocalTime start;
    LocalTime end;
    /**
     * 0:daily,1:weekly,2:monthly,3:yearly,-1:once
     */
    int type;
    /**
     * 1:Monday,2:Tuesday,3:Wednesday,4:Thursday,5:Friday,6:Saturday,7:Sunday
     */
    List<Integer> days;

    public FixedTimeRestriction(String code){
        days = new java.util.ArrayList<>();
        String[] codes=code.split(" ");
        start=LocalTime.parse(codes[0]);
        end=LocalTime.parse(codes[1]);
        type=Integer.parseInt(codes[2]);

        for(int i=3;i<codes.length;i++){
            days.add(Integer.parseInt(codes[i]));
        }
    }
    public FixedTimeRestriction(LocalTime start, LocalTime end, FixedTimeRestrictionType type, List<Integer> days){
        this.start=start;
        this.end=end;
        this.type=type.ordinal();
        this.days=days;
    }
    @NonNull
    public String coding(){
        StringBuilder res= new StringBuilder("FixedTimeRestriction=" + start.toString() + " " + end.toString() + " " + type);
        for(int i=0;i<days.size();i++){
            res.append(" ").append(days.get(i));
        }
        return res.toString();
    }

    @Override
    public boolean check(Task task) {
        return true;
    }


    public LocalTime getStart() {
        return start;
    }

    public void setStart(LocalTime start) {
        this.start = start;
    }

    public LocalTime getEnd() {
        return end;
    }

    public void setEnd(LocalTime end) {
        this.end = end;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<Integer> getDays() {
        return days;
    }

    public void setDays(List<Integer> days) {
        this.days = days;
    }

    public enum FixedTimeRestrictionType {
        DAILY,
        WEEKLY,
        MONTHLY,
        YEARLY
    }
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof FixedTimeRestriction){
            FixedTimeRestriction r=(FixedTimeRestriction)obj;
            return r.start.equals(start) && r.end.equals(end) && r.type==type && r.days.equals(days);
        }
        return false;
    }
}
