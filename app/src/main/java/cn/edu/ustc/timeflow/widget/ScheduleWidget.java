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

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.schedule_widget);

        Intent intent = new Intent(context, TaskWidgetService.class);
        views.setRemoteAdapter(R.id.month_ListView, intent);

        // Create an Intent to update the widget
//        Intent updateIntent = new Intent(context, ScheduleWidget.class);
//        updateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
//        updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[]{appWidgetId});
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, updateIntent, PendingIntent.FLAG_IMMUTABLE);
//        views.setOnClickPendingIntent(R.id.month_ListView, pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        context.startService(new Intent(context, TaskWidgetService.class));
    }

    @Override
    public void onDisabled(Context context) {
        context.stopService(new Intent(context, TaskWidgetService.class));
    }


}