package cn.edu.ustc.timeflow.restriction;

import androidx.annotation.NonNull;

import java.time.LocalDateTime;

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
    public String toString(){
        return "TimeRestriction:"+start.toString()+" "+end.toString();
    }

}
