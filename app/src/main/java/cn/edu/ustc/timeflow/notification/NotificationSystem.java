package cn.edu.ustc.timeflow.notification;

import android.content.Context;

import java.time.LocalDateTime;
import java.util.List;

import cn.edu.ustc.timeflow.bean.Task;
import cn.edu.ustc.timeflow.converter.CalendarLocalDateTimeConverter;
import cn.edu.ustc.timeflow.dao.TaskDao;
import cn.edu.ustc.timeflow.util.DBHelper;

/**
 * 为标记了提醒的事件提供提醒服务
 * 在task前半小时提醒，task结束时提问是否完成
 * 若未标记提醒，则不提醒，且自动标记为完成
 */
public class NotificationSystem {
    public Context context;
    public NotificationSystem(Context context){
        this.context = context;
    }
    public void updateNotification(){
        TaskDao taskDao = new DBHelper(context).getTaskDao();

        List<Task> tasks = taskDao.getByStartDate(LocalDateTime.now());
        for(Task task:tasks) {
            if (task.getRemind() && !task.getFinished()) {
                //提前半小时提醒
                AlarmHelper.setAlarm(context, CalendarLocalDateTimeConverter.INSTANCE.toCalendar(task.getStart()),task.getId(),"reminder");
                //结束时提醒
                AlarmHelper.setAlarm(context, CalendarLocalDateTimeConverter.INSTANCE.toCalendar(task.getEnd()), task.getId(),"finish");
            } else {
                // 结束自动标记为完成
                AlarmHelper.setTaskFinishAlarm(context, CalendarLocalDateTimeConverter.INSTANCE.toCalendar(task.getEnd()), task.getId());
            }
        }
    }
}