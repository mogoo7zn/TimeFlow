package cn.edu.ustc.timeflow.model;

import android.content.Context;

import java.time.LocalDateTime;

import cn.edu.ustc.timeflow.util.TimeTable;

/**
 * 这是一个调度器的抽象类，用于生成时间表
 */
public abstract class Scheduler {
    Valuer valuer;

    Context context;

    public Scheduler(Valuer valuer) {
        this.valuer = valuer;
    }

    public Scheduler(Context context, Valuer valuer) {
        this.context = context;
        this.valuer = valuer;
    }
    /**
     * 获取时间表
     * @param start 开始时间
     * @param end 结束时间
     * @return 时间表
     */
    public abstract TimeTable Schedule(LocalDateTime start, LocalDateTime end);

    /**
     * 生成时间表
     */
    public abstract void Schedule();
}
