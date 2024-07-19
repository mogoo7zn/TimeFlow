package cn.edu.ustc.timeflow.modle;

import cn.edu.ustc.timeflow.util.TimeTable;

public abstract class Scheduler {
    Valuer valuer;

    public Scheduler(Valuer valuer) {
        this.valuer = valuer;
    }

    public abstract TimeTable getTimeTable();
}
