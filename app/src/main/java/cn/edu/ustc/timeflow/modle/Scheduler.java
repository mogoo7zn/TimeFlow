package cn.edu.ustc.timeflow.modle;

import cn.edu.ustc.timeflow.bean.TimeTable;

public abstract class Scheduler {
    public abstract void schedule();
    public abstract TimeTable getTimeTable();
}
