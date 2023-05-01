package org.cop4656.assignment2;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class SMSBroadcastReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "SMS_RECEIVED_CHANNEL";
    private static final int NOTIFICATION_ID = 1;

    DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                String format = bundle.getString("format");
                for (int i = 0; i < pdus.length; i++) {
                    //modify as needed
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdus[i], format);
                    String sender = currentMessage.getDisplayOriginatingAddress();
                    String message = currentMessage.getDisplayMessageBody();
                    String printMessage = "SMS from " + sender + ": " + message;

                    saveSmsToDatabase(context, message, printMessage);

                }
            }
        }
    }

    private boolean isAppInForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> processInfos = activityManager.getRunningAppProcesses();
        if (processInfos != null) {
            for (RunningAppProcessInfo processInfo : processInfos) {
                if (processInfo.processName.equals(context.getPackageName())
                        && processInfo.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isAppInBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> processInfos = activityManager.getRunningAppProcesses();
        if (processInfos != null) {
            for (RunningAppProcessInfo processInfo : processInfos) {
                if (processInfo.processName.equals(context.getPackageName())
                        && processInfo.importance == RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    return true;
                }
            }
        }
        return false;
    }

    private void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
    private void showNotification(Context context, String message) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Create a notification channel (required for API level 26 and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "default_channel_id";
            CharSequence channelName = "Default Channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, importance);
            notificationManager.createNotificationChannel(notificationChannel);
        }


        // Create a notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "default_channel_id")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("New SMS received")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        // Show the notification
        notificationManager.notify(0, builder.build());
    }


    private void saveSmsToDatabase(Context context, String bookMark, String printM) {
        if (bookMark != null) {
            DatabaseReference d = databaseRef.child("texts");
            DatabaseReference g = d.push();

            DatabaseReference k = databaseRef.child("keywords");
            Task<DataSnapshot> t = k.get();
            while (!t.isComplete()) {
            }
            DataSnapshot i = t.getResult();

            int currentIndex = 0;
            boolean has = false;
            for (DataSnapshot childSnapshot : i.getChildren()) {
                if (bookMark.toLowerCase().contains(childSnapshot.getValue().toString().toLowerCase())) {
                    has = true;
                }
                currentIndex++;
            }
            if (has) {
                g.setValue(bookMark);

                if (isAppInForeground(context)) {
                    // App is in foreground, show message in Toast
                    showToast(context, printM);
                    showToast(context, "Message has been added!");
                } else if (isAppInBackground(context)) {
                    // App is in background, show message in notification
                    showNotification(context, printM);
                    showNotification(context, "Message has been added!");
                } else {
                    // App is closed, start MainActivity with message as extra
                    Intent activityIntent = new Intent(context, MainActivity.class);
                    activityIntent.putExtra("sms", bookMark);
                    activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    showNotification(context, printM);
                    showNotification(context, "Message has been added!");
                }
            } else {
                if (isAppInForeground(context)) {
                    // App is in foreground, show message in Toast
                    //showToast(context, printM);
                    showToast(context, "No keyword found!");
                } else if (isAppInBackground(context)) {
                    // App is in background, show message in notification
                    showNotification(context, printM);
                    showNotification(context, "No keyword found!");
                } else {
                    // App is closed, start MainActivity with message as extra
                    Intent activityIntent = new Intent(context, MainActivity.class);
                    activityIntent.putExtra("sms", bookMark);
                    activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    showNotification(context, "No keyword found!");
                }
            }
        }
    }


}