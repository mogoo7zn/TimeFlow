package cn.edu.ustc.timeflow.restriction;

import androidx.annotation.NonNull;

public class RepeatRestriction extends Restriction{
    int total_amount;
    int finished_amount;
    int interval;
    int repeat_times;

    public RepeatRestriction(String code){
        String[] codes=code.split(" ");
        total_amount=Integer.parseInt(codes[0]);
        finished_amount=Integer.parseInt(codes[1]);
        interval=Integer.parseInt(codes[2]);
        repeat_times=Integer.parseInt(codes[3]);
    }
    public RepeatRestriction(int total_amount, int finished_amount, int interval, int repeat_times){
        this.total_amount=total_amount;
        this.finished_amount=finished_amount;
        this.interval=interval;
        this.repeat_times=repeat_times;
    }

    @NonNull
    @Override
    public String toString() {
        return "RepeatRestriction:"+total_amount+" "+finished_amount+" "+interval+" "+repeat_times;
    }
}
