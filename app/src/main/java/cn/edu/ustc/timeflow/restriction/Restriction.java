package cn.edu.ustc.timeflow.restriction;

import cn.edu.ustc.timeflow.bean.Task;

public abstract class Restriction {
    public abstract String coding();
    public abstract boolean check(Task task);
}
