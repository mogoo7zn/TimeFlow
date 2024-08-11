package cn.edu.ustc.timeflow.restriction;

import androidx.annotation.NonNull;

public class RepeatRestriction extends Restriction{
    /**
     * 重复任务的总量
     */
    int total_amount;
    /**
     * 已完成的任务量
     */
    int finished_amount;
    /**
     * 重复任务的间隔（单位：天）
     */
    int interval;
    /**
     * 一个interval内的重复次数
     */
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
    public String coding() {
        return "RepeatRestriction:"+total_amount+" "+finished_amount+" "+interval+" "+repeat_times;
    }

    public int getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(int total_amount) {
        this.total_amount = total_amount;
    }

    public int getFinished_amount() {
        return finished_amount;
    }

    public void setFinished_amount(int finished_amount) {
        this.finished_amount = finished_amount;
    }

    public int getInterval() {
        return interval;
    }
    public void setInterval(int interval) {
        this.interval = interval;
    }
    public int getRepeat_times() {
        return repeat_times;
    }
    public void setRepeat_times(int repeat_times) {
        this.repeat_times = repeat_times;
    }
}
