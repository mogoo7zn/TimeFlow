package cn.edu.ustc.timeflow.model;

import java.time.LocalDateTime;

import cn.edu.ustc.timeflow.bean.Action;

/**
 * 估值器用于估算任务的优先级
 * 这里的优先级是一个double值，值越大，优先级越高
 * 优先级的计算方法由具体的实现类决定
 * 若value<0，则任务不会被加入时间表
 * @see StandardValuer
 * @see SimpleValuer
 */
public interface Valuer {
    public double valuate(Action action, LocalDateTime time);
}
