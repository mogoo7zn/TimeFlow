package cn.edu.ustc.timeflow.notification;

import android.content.Context;

import java.time.LocalDateTime;
import java.util.Calendar;
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
public class AlarmSystem {
    public Context context;
    public AlarmSystem(Context context){
        this.context = context;
    }
    public void setAlarms(){
        TaskDao taskDao = new DBHelper(context).getTaskDao();
        //TODO: clear all alarms


        //TODO: set alarms for all tasks
        List<Task> tasks = taskDao.getByStartDate(LocalDateTime.now());
        for(Task task:tasks) {
            if (task.getRemind()) {

                //提前半小时提醒
                AlarmHelper.setAlarm(context, CalendarLocalDateTimeConverter.INSTANCE.toCalendar(task.getStart()), "任务提醒", task.getContent());
                //结束时提醒
                AlarmHelper.setAlarm(context, CalendarLocalDateTimeConverter.INSTANCE.toCalendar(task.getEnd()), "任务结束", "任务结束，是否完成？");
            } else {
                // 结束自动标记为完成
                AlarmHelper.setAlarm(context, CalendarLocalDateTimeConverter.INSTANCE.toCalendar(task.getEnd()), task.getId());
            }
        }
    }
}
