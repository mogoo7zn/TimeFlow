package cn.edu.ustc.timeflow.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import java.util.Objects;

import cn.edu.ustc.timeflow.bean.Task;
import cn.edu.ustc.timeflow.dao.TaskDao;
import cn.edu.ustc.timeflow.util.DBHelper;

public class TaskFinishReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int taskId = intent.getIntExtra("task_id", -1);
        if(taskId == -1){
            Log.e("TaskFinishReceiver", "task_id not found");
            return;
        }
        TaskDao taskDao = new DBHelper(context).getTaskDao();
        Task task = taskDao.getById(taskId);
        assert task != null;
        task.setFinished(true);
        taskDao.update(task);
    }
}
