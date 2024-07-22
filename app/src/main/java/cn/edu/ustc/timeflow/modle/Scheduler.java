package cn.edu.ustc.timeflow.modle;

import java.time.LocalDate;
import java.time.LocalDateTime;

import cn.edu.ustc.timeflow.util.TimeTable;

/**
 * 这是一个调度器的抽象类，用于生成时间表
 */
public abstract class Scheduler {
    Valuer valuer;

    public Scheduler(Valuer valuer) {
        this.valuer = valuer;
    }

    public abstract TimeTable getTimeTable(LocalDateTime start, LocalDateTime end);

}
