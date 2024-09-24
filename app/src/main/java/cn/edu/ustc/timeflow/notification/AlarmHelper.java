package cn.edu.ustc.timeflow.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.example.timeflow.R;

import java.util.Calendar;

public class AlarmHelper {
    private static boolean isRequestScheduleExactAlarmStarted = false;

    // This method is used to set an alarm for a task (before the task starts)
    public static void setAlarm(Context context, Calendar calendar, int TaskId, String type) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                Intent intent = new Intent(context, NotificationReceiver.class);
                intent.putExtra("task_id", TaskId);
                intent.putExtra("type", type);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, TaskId, intent, PendingIntent.FLAG_IMMUTABLE );
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                Log.d("AlarmHelper", "setAlarm: " + calendar.getTime());
            } else {
                Toast.makeText(context, R.string.alarm_premission_not_granted, Toast.LENGTH_LONG).show();
                if (!isRequestScheduleExactAlarmStarted) {
                    isRequestScheduleExactAlarmStarted = true;
                    Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                    context.startActivity(intent);
                }
            }
        }
    }

    // This method is used to set an alarm for a task (after the task finishes)
    public static void setTaskFinishAlarm(Context context, Calendar calendar, int TaskId) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                Intent intent = new Intent(context, TaskFinishReceiver.class);
                intent.putExtra("task_id", TaskId);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                Log.d("AlarmHelper", "setTaskFinishAlarm: " + calendar.getTime());
            } else {
                Toast.makeText(context, R.string.alarm_premission_not_granted, Toast.LENGTH_LONG).show();
                if (!isRequestScheduleExactAlarmStarted) {
                    isRequestScheduleExactAlarmStarted = true;
                    Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                    context.startActivity(intent);
                }
            }
        }
    }
}