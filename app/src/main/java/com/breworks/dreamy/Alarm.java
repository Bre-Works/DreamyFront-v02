package com.breworks.dreamy;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.breworks.dreamy.model.Todo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by arsianindita on 26-Nov-14.
 */


public class Alarm extends BroadcastReceiver {

    Date deadline;
    private PendingIntent alarmIntent;
    int onRecID;
    String onRecTask, onRecDeadline;

    @Override
    public void onReceive(Context context, Intent intent) {
            Log.e("The onReceive method in Alarm class is called", "Be joyful");
            //Toast.makeText(context, "It's time", Toast.LENGTH_SHORT).show();
            Bundle extras = intent.getExtras();

            onRecID = extras.getInt("theID", 0);
            onRecTask = extras.getString("theTask");
            onRecDeadline = extras.getString("theDate");

            Log.e("The ID onReceive is", String.valueOf(onRecID));

            callNotification(context, onRecID, onRecTask, onRecDeadline);

            Todo t = new Todo();
            Long longID = (long) onRecID;
            t.findByTaskID(longID).setNotifStatus(false);

    }

    public void setAlarm(Context context, Todo t) {
        Log.e("The setAlarm method in Alarm class is called", "Hurray");

        deadline = t.getDeadline(t);
        int ID = t.getId().intValue();

        Log.e("The ID is:", String.valueOf(ID));
        Log.e("The deadline is:", String.valueOf(deadline));

        AlarmManager a = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, Alarm.class);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy/hh:mm");
        String dateString = dateFormat.format(deadline);

        intent.putExtra("theID", ID);
        intent.putExtra("theTask", t.getTask(t));
        intent.putExtra("theDate", dateString);

        alarmIntent = PendingIntent.getBroadcast(context, ID, intent, 0);
        a.set(AlarmManager.RTC_WAKEUP, deadline.getTime(), alarmIntent);
    }

    public void cancelAlarm(Context context, Todo t) {
        Log.e("The cancelAlarm method in Alarm class is called", "Now smile!");
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, Alarm.class);

        int ID = t.getId().intValue();

        PendingIntent p = PendingIntent.getBroadcast(context, ID, i, 0);
        am.cancel(p);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void callNotification(Context context, int notificationID, String task, String deadlineString){

        Log.e("The callNotification is called", "oh really?");
        Log.e("The current ID in Alarm is", String.valueOf(notificationID));

        Intent intent = new Intent(context, NotificationR.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, notificationID, intent, 0);



        Notification n = new Notification.Builder(context)
                .setContentTitle(task)
                .setContentText(deadlineString)
                .setSmallIcon(R.drawable.dreamlogo)
                .setContentIntent(pIntent).build();

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        nm.notify(notificationID, n);
    }
}

