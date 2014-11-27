package com.breworks.dreamy;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import java.util.Date;

/**
 * Created by arsianindita on 26-Nov-14.
 */


public class Alarm extends BroadcastReceiver{

    Date deadline;
    private PendingIntent alarmIntent;

    @Override
    public void onReceive(Context context, Intent intent) {
            Log.e("The onReceive method in Alarm class is called", "Be joyful");
            Toast.makeText(context, "It's time", Toast.LENGTH_SHORT).show();
    }

    public void setAlarm(Context context){
        Log.e("The setAlarm method in Alarm class is called", "Hurray");
        Log.e("The deadline is:", String.valueOf(deadline));
        AlarmManager a = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context,Alarm.class);
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        a.set(AlarmManager.RTC_WAKEUP, deadline.getTime(), alarmIntent);
    }

    public void cancelAlarm(Context context){
        Log.e("The cancelAlarm method in Alarm class is called", "Now smile!");
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context,Alarm.class);
        PendingIntent p = PendingIntent.getBroadcast(context, 0, i, 0);
        am.cancel(p);
    }

    public void setDeadline(Date deadline){
        this.deadline = deadline;}
    }

