package cn.edu.ustc.timeflow.restriction;

import cn.edu.ustc.timeflow.bean.Task;

public class IntervalRestriction extends Restriction{

    /**
     * 重复任务的间隔（单位：天）
     */
    int interval;
    /**
     * 一个interval内的重复次数
     */
    int repeat_times;
    public IntervalRestriction(String code){
        String[] codes=code.split(" ");
        interval=Integer.parseInt(codes[0]);
        repeat_times=Integer.parseInt(codes[1]);
    }
    public IntervalRestriction(int interval,int repeat_times){
        this.interval = interval;
        this.repeat_times = repeat_times;
    }

    @Override
    public String coding() {
        return "IntervalRestriction="+interval+" "+repeat_times;
    }

    @Override
    public boolean check(Task task) {
        return false;
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
