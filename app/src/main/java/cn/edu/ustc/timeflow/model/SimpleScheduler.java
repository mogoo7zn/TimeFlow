package cn.edu.ustc.timeflow.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import cn.edu.ustc.timeflow.bean.Action;
import cn.edu.ustc.timeflow.bean.Task;
import cn.edu.ustc.timeflow.dao.ActionDao;
import cn.edu.ustc.timeflow.database.ActionDB;
import cn.edu.ustc.timeflow.util.TimeTable;

/**
 * 这是一个简单的调度器，它会将所有固定任务加入时间表，然后对非固定任务进行排序，按照价值最高的顺序加入时间表
 */
public class SimpleScheduler extends Scheduler {


    public SimpleScheduler(Valuer valuer) {
        super(valuer);
    }

    /**
     * - 先安排固定时间任务（Fixed Model）
     *     - 调出所有含Fixed（通过Type判断）的任务，检查时间范围是否符合其他限制，符合则加入。
     * - 按照Value所得的优先级，依此向时间表填充任务
     *     - 除去上述任务，按Value排序
     *     - 取出当前空余时间
     *     - 检查是否满足其他限制，并依此填充，优先填大于该任务duration的最短时段
     * - 同步时间表至数据库
     * @param start 开始时间
     * @param end 结束时间
     * @return 时间表
     */
    @Override
    public TimeTable getTimeTable(LocalDateTime start, LocalDateTime end) {
        TimeTable timeTable = new TimeTable(context, start, end);

        ActionDao actionDao = ActionDB.Companion.getDatabase(context).actionDao();

        List<Action> actions = actionDao.getAll();

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
