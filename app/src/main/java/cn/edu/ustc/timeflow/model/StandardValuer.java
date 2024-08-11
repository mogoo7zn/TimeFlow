package cn.edu.ustc.timeflow.model;

import android.content.Context;

import androidx.annotation.Nullable;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import cn.edu.ustc.timeflow.bean.Action;
import cn.edu.ustc.timeflow.bean.Task;
import cn.edu.ustc.timeflow.dao.TaskDao;
import cn.edu.ustc.timeflow.restriction.RepeatRestriction;
import cn.edu.ustc.timeflow.restriction.TimeRestriction;
import cn.edu.ustc.timeflow.util.DBHelper;

///其中权重运算法则包含两条，即绝对紧迫性和相对紧迫性。前者代表和时间节点实际间隔长短，是确定值；后者有两种情况，一是剩余任务量分配给指定剩余时间造成的紧迫性，二是未规划任务虽时间积累而变的紧迫性（比如体育锻炼，其紧迫性会与上一次体育锻炼的时间间隔变长而随之变大），有默认的紧迫性指标（比如一周五次）
//
//设任务量 W ∝ 任务时间 T ，绝对紧迫性为Ia，相对紧迫性为Ir，离ddl时间dt，绝对紧迫比重wa(absolute)，相对紧迫比重wr(relative)，其默认紧迫指标wrb(base)
//
//有公式
//
//$$
//t_k = la_k+lr_k=\sum_{i=1}^n wa_k \cdot W_k + wr_k \cdot W_k
//$$
//
//其中
//
//$$
//wa_k =\frac 1 {dt^α}
//$$
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
        RepeatRestriction repeatRestriction=(RepeatRestriction) action.getRestriction("RepeatRestriction");
        if(repeatRestriction==null){
            return 0;
        }
        //workload=剩余任务量*任务时间
        double workload=(repeatRestriction.getTotal_amount()-repeatRestriction.getFinished_amount())*action.getDuration().toMinutes();
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

        RepeatRestriction repeatRestriction=(RepeatRestriction) action.getRestriction("RepeatRestriction");
        if(repeatRestriction==null){
            return 0;
        }
        LocalDateTime now= LocalDateTime.now();
        LocalDateTime lastTime = getLastTime(action);
        if (lastTime == null) return 0; // or handle the case where there are no tasks
        LocalDateTime intervalEndTime = lastTime.plusDays(repeatRestriction.getInterval());

        TaskDao taskDao=new DBHelper(context).getTaskDao();

        int leftTimes = taskDao.countByActionIdWithTime(
            action.getId(),
            now.minusDays(repeatRestriction.getInterval()),
            now
        );

        return (double) leftTimes / Duration.between(now, intervalEndTime).toMinutes();
    }
}
