package cn.edu.ustc.timeflow.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.timeflow.R;

import cn.edu.ustc.MainActivity;

public class ScheduleWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.month_ListView);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.schedule_widget);
            Intent intent = new Intent(context, TaskWidgetService.class);
            views.setRemoteAdapter(R.id.month_ListView, intent);

            //TODO: 添加点击事件，跳转到主界面
            Intent mainIntent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, mainIntent, PendingIntent.FLAG_IMMUTABLE);
            views.setPendingIntentTemplate(R.id.widget_items, pendingIntent);


            Log.d("ScheduleWidget", "onUpdate");
            appWidgetManager.updateAppWidget(appWidgetId, views);

        }

    }

    @Override
    public void onEnabled(Context context) {
        context.startService(new Intent(context, TaskWidgetService.class));
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(intent.getAction())) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
            if (appWidgetIds != null) {
                onUpdate(context, appWidgetManager, appWidgetIds);

            }

        }

    }

}