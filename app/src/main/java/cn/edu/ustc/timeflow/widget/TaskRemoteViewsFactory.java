package cn.edu.ustc.timeflow.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.timeflow.R;

import java.util.List;

import cn.edu.ustc.timeflow.bean.Task;

public class TaskRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private final Context context;
    private List<Task> taskList;

    public TaskRemoteViewsFactory(Context context, Intent intent, List<Task> taskList) {
        this.context = context;
        this.taskList = taskList;
    }

    @Override
    public void onCreate() {
        // Initialize taskList here
    }

    @Override
    public void onDataSetChanged() {
        // Update taskList here
    }

    @Override
    public void onDestroy() {
        // Clean up resources
    }

    @Override
    public int getCount() {
        return taskList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Task task = taskList.get(position);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_item);
        views.setTextViewText(R.id.lesson_text, task.getContent());
        views.setTextViewText(R.id.lesson_teacher, task.getEvaluation());
        views.setTextViewText(R.id.lesson_room, String.valueOf(task.getImportance()));
        views.setTextViewText(R.id.lesson_time, task.getStart().toString() + " - " + task.getEnd().toString());
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}