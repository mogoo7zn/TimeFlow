package cn.edu.ustc.timeflow.restriction;

import androidx.annotation.NonNull;

import java.time.LocalTime;
import java.util.List;

public class FixedTimeRestriction extends Restriction{
    LocalTime start;
    LocalTime end;
    int type;//0:daily,1:weekly,2:monthly,3:yearly
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
    public FixedTimeRestriction(LocalTime start, LocalTime end, int type, List<Integer> days){
        this.start=start;
        this.end=end;
        this.type=type;
        this.days=days;
    }
    @NonNull
    public String coding(){
        StringBuilder res= new StringBuilder("FixedTimeRestriction:" + start.toString() + " " + end.toString() + " " + type);
        for(int i=0;i<days.size();i++){
            res.append(" ").append(days.get(i));
        }
        return res.toString();
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
}
