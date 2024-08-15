package cn.edu.ustc.timeflow.model;

import cn.edu.ustc.timeflow.bean.Action;

/**
 * 估值器用于估算任务的优先级
 * 这里的优先级是一个double值，值越大，优先级越高
 * 优先级的计算方法由具体的实现类决定
 * @see StandardValuer
 * @see SimpleValuer
 */
public interface Valuer {
    public double valuate(Action action);
}
