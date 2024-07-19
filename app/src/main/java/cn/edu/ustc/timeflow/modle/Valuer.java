package cn.edu.ustc.timeflow.modle;

import cn.edu.ustc.timeflow.bean.Action;

public interface Valuer {
    public double value(Action action);
}
