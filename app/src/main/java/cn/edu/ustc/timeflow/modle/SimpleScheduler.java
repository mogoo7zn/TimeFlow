package cn.edu.ustc.timeflow.modle;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import cn.edu.ustc.timeflow.bean.Action;
import cn.edu.ustc.timeflow.bean.Task;
import cn.edu.ustc.timeflow.data.ActionData;
import cn.edu.ustc.timeflow.util.TimeTable;

public class SimpleScheduler extends Scheduler {


    public SimpleScheduler(Valuer valuer) {
        super(valuer);
    }

    @Override
    public TimeTable getTimeTable(LocalDateTime start, LocalDateTime end) {
        TimeTable timeTable = new TimeTable(start, end);
        List<Action> actions = ActionData.getUnfinishedActions();
        //遍历所有任务，将固定任务加入时间表
        for (Action action : actions) {
            if(action.getType().equals("fixed")){
                timeTable.AddTask(new Task(action, start, end));
            }
        }
        //对非固定任务进行排序
        actions.sort((o1, o2) -> {
            return Double.compare(valuer.valuate(o2), valuer.valuate(o1));
        });
        //遍历非固定任务，将其加入时间表
        for (Action action : actions) {

            if(action.getType().equals("flexible")){
                List<kotlin.Pair<LocalDateTime, LocalDateTime>> AvailableTime = timeTable.getAvailableTime();
                //遍历时间表，找到第一个合适的时间段，将任务加入时间表
                for (kotlin.Pair<LocalDateTime, LocalDateTime> pair : AvailableTime) {
                    LocalDateTime start1 = pair.getFirst();
                    LocalDateTime end1 = pair.getSecond();
                    if(Duration.between(start1, end1).toMinutes() > action.getDuration().toMinutes()){
                        Task task = new Task(action, start1, start1.plus(action.getDuration()));
                        timeTable.AddTask(task);
                        break;
                    }

                }
            }
        }
        return timeTable;
    }

}
