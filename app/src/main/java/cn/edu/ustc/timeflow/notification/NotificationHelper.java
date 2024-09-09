package cn.edu.ustc.timeflow.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.example.timeflow.R;

public class NotificationHelper {
    private static final String CHANNEL_ID = "default";
    private static final String CHANNEL_NAME = "Default Channel";

    public static void sendNotification(Context context, String title, String content) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Create the notification channel if necessary
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
        notificationManager.createNotificationChannel(channel);

        // Build and send the notification
        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .build();

        notificationManager.notify(100, notification);
    }

    public static void sendTaskNotification(Context context, String title, String content, int taskId) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Create the notification channel if necessary
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
        notificationManager.createNotificationChannel(channel);

        // Create an intent for the button action
        Intent buttonIntent = new Intent(context, TaskFinishReceiver.class);
        buttonIntent.putExtra("action", "button_action");
        buttonIntent.putExtra("task_id", taskId);
        PendingIntent buttonPendingIntent = PendingIntent.getBroadcast(context, 0, buttonIntent, PendingIntent.FLAG_IMMUTABLE);

        // Build and send the notification
        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                //TODO：soft code
                .addAction(R.drawable.ic_flag, "已完成", buttonPendingIntent) // Add button here
                .build();

        notificationManager.notify(100, notification);
    }
}