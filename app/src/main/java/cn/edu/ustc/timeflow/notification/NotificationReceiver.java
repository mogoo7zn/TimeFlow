package cn.edu.ustc.timeflow.notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import cn.edu.ustc.timeflow.bean.Task;
import cn.edu.ustc.timeflow.dao.TaskDao;
import cn.edu.ustc.timeflow.util.DBHelper;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        TaskDao taskDao = new DBHelper(context).getTaskDao();



        int task_id = intent.getIntExtra("task_id", -1);
        String type = intent.getStringExtra("type");
        Task task = taskDao.getById(task_id);

        if(task_id == -1 || task == null || task.getFinished() || !task.getRemind()) {
            return;
        }

        assert type != null;
        // TODO: 通知内容
        String title = task.getContent();
        String content = task.getNote();
        if(type.equals("reminder")) {
            NotificationHelper.sendNotification(context, title, content);
        }
        else if(type.equals("finish")) {
            NotificationHelper.sendTaskNotification(context, title, content, task_id);
        }

    }
}
