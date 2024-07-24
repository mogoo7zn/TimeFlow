package cn.edu.ustc.timeflow.model;

import cn.edu.ustc.timeflow.bean.Action;

public interface Valuer {
    public double valuate(Action action);
}
