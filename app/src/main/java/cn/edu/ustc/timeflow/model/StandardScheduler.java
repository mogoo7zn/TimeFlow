package cn.edu.ustc.timeflow.model;

import android.content.Context;
import android.util.Log;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.PriorityQueue;

import cn.edu.ustc.timeflow.bean.Action;
import cn.edu.ustc.timeflow.bean.Task;
import cn.edu.ustc.timeflow.dao.ActionDao;
import cn.edu.ustc.timeflow.restriction.FixedTimeRestriction;
import cn.edu.ustc.timeflow.restriction.Restriction;
import cn.edu.ustc.timeflow.util.DBHelper;
import cn.edu.ustc.timeflow.util.TimeTable;

public class StandardScheduler extends Scheduler{
    ActionDao actionDao;

    public StandardScheduler(Context context, Valuer valuer) {
        super(context, valuer);
        actionDao = new DBHelper(context).getActionDao();
    }

    @Override
    public TimeTable Schedule(LocalDateTime start, LocalDateTime end) {
        for (LocalDate date = start.toLocalDate(); date.isBefore(end.toLocalDate()); date = date.plusDays(1)){
            TimeTable timeTable = new TimeTable(context, date);
            RemoveUnfinishedFutureTask(timeTable, date);
            FixedTaskHandler(timeTable, date);
            RepeatingTaskHandler(timeTable,date);
            timeTable.sync();
        }
        return null;
    }

    @Override
    public void Schedule() {
    }

    private void RemoveUnfinishedFutureTask(TimeTable timeTable, LocalDate date) {
        // 删除未完成的未来任务
        timeTable.getTasks().removeIf(task -> task.getStart().isAfter(LocalDateTime.of(date, LocalTime.MIN)) && !task.getFinished());
        timeTable.sync();
    }

    private void FixedTaskHandler(TimeTable timeTable, LocalDate date) {
        // 获取所有固定任务
        // 检查时间范围是否符合其他限制
        // 符合则加入
        List<Action> actions = actionDao.getByType("Fixed");
        for (Action action : actions) {
            if (action.getRestriction("FixedTimeRestriction") != null) {
                //获取固定时间限制
                List<Restriction> restrictions = action.getRestrictions("FixedTimeRestriction");
                for (Restriction restriction : restrictions) {

                    FixedTimeRestriction fixedTimeRestriction = (FixedTimeRestriction) restriction;

                    switch (fixedTimeRestriction.getType()) {//0:daily,1:weekly,2:monthly,3:yearly，4:once
                        case 0:
                            AddFixedTask(timeTable, date, action, fixedTimeRestriction);
                            break;
                        case 1:
                            if (fixedTimeRestriction.getDays().contains(date.getDayOfWeek().getValue())) {

                                AddFixedTask(timeTable, date, action, fixedTimeRestriction);
                            }
                            break;
                        case 2:
                            if (fixedTimeRestriction.getDays().contains(date.getDayOfMonth()))
                                AddFixedTask(timeTable, date, action, fixedTimeRestriction);
                            break;
                        case 3:
                            if (fixedTimeRestriction.getDays().contains(date.getDayOfYear()))
                                AddFixedTask(timeTable, date, action, fixedTimeRestriction);
                            break;
                        case 4:
                                AddFixedTask(timeTable, date, action, fixedTimeRestriction);
                            break;
                    }
                }
            }
            timeTable.sync();
        }
    }
    private void AddFixedTask(TimeTable timeTable, LocalDate date, Action action, FixedTimeRestriction fixedTimeRestriction) {
        if(fixedTimeRestriction.getStart().isAfter(fixedTimeRestriction.getEnd())) {
            //跨天，分两次加入
            LocalDateTime start1 = LocalDateTime.of(date, fixedTimeRestriction.getStart());
            LocalDateTime end1 = LocalDateTime.of(date, LocalTime.of(23, 59, 0));
            Task task = new Task(action, start1, end1);
            if(timeTable.getTasks().contains(task) || !new RestrictionChecker(context, action, task).RestrictionCheck())
                return;
            timeTable.addTask(task);

            LocalDateTime start2 = LocalDateTime.of(date, LocalTime.of(0, 0, 1));
            LocalDateTime end2 = LocalDateTime.of(date, fixedTimeRestriction.getEnd());
            Task task2 = new Task(action, start2, end2);
            if(timeTable.getTasks().contains(task2) || !new RestrictionChecker(context, action, task2).RestrictionCheck())
                return;
            timeTable.addTask(task2);
        }
        else {
            //不跨天
            LocalDateTime start1 = LocalDateTime.of(date, fixedTimeRestriction.getStart());
            LocalDateTime end1 = LocalDateTime.of(date, fixedTimeRestriction.getEnd());
            Task task = new Task(action, start1, end1);
            //如果timetable中没有这个任务，且符合限制，则加入
            if(!timeTable.getTasks().contains(task) && new RestrictionChecker(context, action, task).RestrictionCheck())
                timeTable.addTask(task);
        }
    }
    private void RepeatingTaskHandler(TimeTable timeTable, LocalDate date) {
        // 获取所有重复任务
        // 检查时间范围是否符合其他限制
        // 符合则加入

        PriorityQueue<Action> actions = new PriorityQueue<>((o1, o2) -> Double.compare(valuer.valuate(o2,LocalDateTime.of(date,LocalTime.of(12,0))), valuer.valuate(o1,LocalDateTime.of(date,LocalTime.of(12,0)))));

            actions.addAll(actionDao.getByType("Repeating"));
        
        int maxIterations = 30; // Maximum iterations
        int iterationCount = 0;

        while (!actions.isEmpty() && iterationCount < maxIterations) {
            Action action = actions.poll();
            List<kotlin.Pair<LocalDateTime, LocalDateTime>> availableTime = timeTable.getAvailableTime();

            for (kotlin.Pair<LocalDateTime, LocalDateTime> pair : availableTime) {
                LocalDateTime start = pair.getFirst();
                LocalDateTime end = pair.getSecond();
                Duration duration = Duration.between(start, end);

                assert action != null;
                if (duration.toMinutes() >= action.getDuration().toMinutes()) {
                    Task task = new Task(action, start, start.plus(action.getDuration()));
                    if (new RestrictionChecker(context, action, task).RestrictionCheck()) {
                        timeTable.addTask(task);
                    }
                    break;
                }
            }

            timeTable.sync();
            if (valuer.valuate(action, LocalDateTime.of(date, LocalTime.of(12, 0))) > 0) {
                actions.add(action);
            }

            iterationCount++;
        }

        if (iterationCount >= maxIterations) {
            Log.w("StandardScheduler", "Reached maximum iteration count, possible infinite loop detected.");
        }
        timeTable.sync();
    }

}
