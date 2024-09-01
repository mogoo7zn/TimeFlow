package cn.edu.ustc.timeflow.model;

import android.content.Context;

import androidx.annotation.Nullable;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import cn.edu.ustc.timeflow.bean.Action;
import cn.edu.ustc.timeflow.bean.Task;
import cn.edu.ustc.timeflow.dao.TaskDao;
import cn.edu.ustc.timeflow.restriction.AmountRestriction;
import cn.edu.ustc.timeflow.restriction.IntervalRestriction;
import cn.edu.ustc.timeflow.restriction.TimeRestriction;
import cn.edu.ustc.timeflow.util.DBHelper;
/**
    * 标准估值器
 * 估值器用于估算任务的紧迫性
 * 紧迫性=绝对紧迫性+相对紧迫性
 * 绝对紧迫性=1/剩余时间^alpha
 * 相对紧迫性=工作量紧迫性+间隔紧迫性+重复紧迫性
 * 工作量紧迫性=剩余任务量*任务时间/剩余时间
 * 间隔紧迫性=当前时间-上次任务时间
 * 重复紧迫性=剩余次数/间隔时间
 * alpha为紧迫性权重
 * @see Valuer
 */
public class StandardValuer implements Valuer{
    /**
     * 紧迫性权重
     */
    static double alpha=0.5;
    Context context;
    public StandardValuer(Context context){
        this.context=context;
    }
    public double valuate(Action action){
        return getAbsoluteUrgency(action)+getRelativeUrgency(action);
    }

    public double getAbsoluteUrgency(Action action){
        return 1/Math.pow(getRemainingTime(action),alpha);

    }

    public double getRelativeUrgency(Action action){
        return getWorkloadUrgency(action)+getIntervalUrgency(action)+getRepeatUrgency(action);
    }
    /**
     * 获取任务的工作量紧迫性
     * @param action 任务
     * @return 工作量紧迫性
     */
    public double getWorkloadUrgency(Action action){

        AmountRestriction amountRestriction = (AmountRestriction) action.getRestriction("AmountRestriction");
        if (amountRestriction==null){
            return 0;
        }
        double workload = (amountRestriction.getTotal()-amountRestriction.getFinished())*action.getDuration().toMinutes();
        return workload/getRemainingTime(action);
    }

    /**
     * 获取任务的剩余时间(分钟)
     * @param action 任务
     * @return 剩余时间
     */
    public double getRemainingTime(Action action){
        TimeRestriction timeRestriction=(TimeRestriction) action.getRestriction("TimeRestriction");
        if(timeRestriction==null){
            return 0;
        }
        LocalDateTime now= LocalDateTime.now();
        LocalDateTime end=timeRestriction.getEnd();
        return Duration.between(now,end).toMinutes();
    }


    /**
     * 获取任务的间隔紧迫性
     * @param action 任务
     * @return 间隔紧迫性
     */
    public double getIntervalUrgency(Action action){
        LocalDateTime lastTime = getLastTime(action);
        if (lastTime == null) return 0; // or handle the case where there are no tasks
        LocalDateTime now = LocalDateTime.now();
        return Duration.between(lastTime, now).toMinutes();
    }

    private @Nullable LocalDateTime getLastTime(Action action) {
        TaskDao taskDao=new DBHelper(context).getTaskDao();
        List<Task> tasks = taskDao.getByActionId(action.getId());
        if (tasks.isEmpty()) {
            return null;
        }
        return tasks.get(tasks.size() - 1).getEnd();
    }

    /**
     * 获取任务的重复紧迫性
     * @param action 任务
     * @return 重复紧迫性
     */
    public double getRepeatUrgency(Action action){

        IntervalRestriction intervalRestriction=(IntervalRestriction) action.getRestriction("IntervalRestriction");

        if(intervalRestriction==null){
            return 0;
        }
        LocalDateTime now= LocalDateTime.now();
        LocalDateTime lastTime = getLastTime(action);
        if (lastTime == null) return 0; // or handle the case where there are no tasks
        LocalDateTime intervalEndTime = lastTime.plusDays(intervalRestriction.getInterval());

        TaskDao taskDao=new DBHelper(context).getTaskDao();

        int leftTimes = taskDao.countByActionIdWithTime(
            action.getId(),
            now.minusDays(intervalRestriction.getInterval()),
            now
        );

        return (double) leftTimes / Duration.between(now, intervalEndTime).toMinutes();
    }
}
