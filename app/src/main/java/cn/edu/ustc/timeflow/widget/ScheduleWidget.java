package cn.edu.ustc.timeflow.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.example.timeflow.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cn.edu.ustc.MainActivity;

public class ScheduleWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.month_ListView);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.schedule_widget);
//            TextView WidgetTitle = views.findViewById(R.id.widget_title);

            // 设置标题为当前日期
            String currentDate = new SimpleDateFormat("yyyy-MM-dd",
                    Locale.getDefault()).format(new Date());
            views.setTextViewText(R.id.widget_title, currentDate);

            // 跳转到主界面
            Intent mainIntent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context,
                    0,
                    mainIntent,
                    PendingIntent.FLAG_IMMUTABLE);
            views.setOnClickPendingIntent(R.id.widget_container, pendingIntent);

            // 为ListView设置适配器
            Intent intent = new Intent(context, TaskWidgetService.class);
            views.setRemoteAdapter(R.id.month_ListView, intent);


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
