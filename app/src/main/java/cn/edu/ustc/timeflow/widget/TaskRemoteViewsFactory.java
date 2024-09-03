package cn.edu.ustc.timeflow.widget;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.timeflow.R;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

import cn.edu.ustc.timeflow.bean.Task;
import cn.edu.ustc.timeflow.util.TimeTable;

public class TaskRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private final Context context;
    private List<Task> taskList;
    private void update(Context context) {
        TimeTable timeTable = new TimeTable(context, LocalDate.now());
        this.taskList = timeTable.getTasks();
    }

    public TaskRemoteViewsFactory(Context context, Intent intent) {
        this.context = context;
        update(context);
    }

    @Override
    public void onCreate() {
        update(context);

    }

    @Override
    public void onDataSetChanged() {
        update(context);

    }

    @Override
    public void onDestroy() {
        taskList.clear();
    }

    @Override
    public int getCount() {
        return taskList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        Task task = taskList.get(position);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_item);
        views.setTextViewText(R.id.lesson_text, task.getContent());
        views.setTextViewText(R.id.lesson_teacher, task.getNote());
        views.setTextViewText(R.id.lesson_room, String.valueOf(task.getLocation()));
        views.setTextViewText(R.id.lesson_time, task.getStart().format(formatter) + " - " + task.getEnd().format(formatter));
        views.setOnClickFillInIntent(R.id.widget_items, new Intent());
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