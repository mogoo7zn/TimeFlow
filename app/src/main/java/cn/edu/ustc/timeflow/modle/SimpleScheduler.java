package cn.edu.ustc.timeflow.modle;

import java.time.LocalDateTime;

import cn.edu.ustc.timeflow.util.TimeTable;

public class SimpleScheduler extends Scheduler {


    public SimpleScheduler(Valuer valuer) {
        super(valuer);
    }

    @Override
    public TimeTable getTimeTable(LocalDateTime start, LocalDateTime end) {

        return null;
    }
}
