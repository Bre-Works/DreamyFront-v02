package com.breworks.dreamy;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.breworks.dreamy.model.Todo;

import java.util.List;

/**
 * Created by arsianindita on 04-Dec-14.
 */
public class OnBoot extends BroadcastReceiver{


    @Override
    public void onReceive(Context context, Intent intent) {
            Log.e("onBoot is called!", "...");
            List<Todo> tds = Todo.searchByNotifStatus(true);
            Alarm a = new Alarm();
            for(Todo t : tds){
                a.setAlarm(context, t);
            }
    }
}
