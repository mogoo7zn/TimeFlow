package cn.edu.ustc.timeflow.data;

import androidx.annotation.Nullable;

import org.jetbrains.annotations.Contract;

import java.time.LocalDateTime;
import java.util.List;

import cn.edu.ustc.timeflow.bean.Action;

public class ActionData {

    public static void addAction(Action action) {
    }
    public static void updateAction(Action action) {
    }
    public static void deleteAction(Action action) {
    }
    public static List<Action> getActionByGoal(int goal_id) {
        return null;
    }
    public static Action getActionByID(int id) {
        return null;
    }
    public static List<Action> getActionByTime(LocalDateTime start, LocalDateTime end) {
        return null;
    }

    @Contract(pure = true)
    public static List<Action> getUnfinishedActions() {
        return null;
    }
}
