package cn.edu.ustc.timeflow.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

import java.time.LocalDate;

import cn.edu.ustc.timeflow.util.TimeTable;

public class TaskWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {


        TimeTable timeTable=new TimeTable(this.getApplicationContext(), LocalDate.now());

        return new TaskRemoteViewsFactory(this.getApplicationContext(), intent, timeTable.getTasks());
    }
}