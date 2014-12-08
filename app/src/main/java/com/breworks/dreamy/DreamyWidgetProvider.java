package com.breworks.dreamy;

import android.app.Activity;
import android.appwidget.AppWidgetProvider;

/**
 * Created by R A W on 11/27/2014.
 */

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.breworks.dreamy.model.Dream;
import com.breworks.dreamy.model.dreamyAccount;

import java.util.List;
import java.util.Random;

public class DreamyWidgetProvider extends AppWidgetProvider {
    private static final String ACTION_CLICK = "ACTION_CLICK";
    //SessionManager session;
    //dreamyAccount login;
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {

        // Get all ids
        ComponentName thisWidget = new ComponentName(context,
                DreamyWidgetProvider.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        for (int widgetId : allWidgetIds) {
            //session = new SessionManager(getApplicationContext());
            //login = session.getUser();
            List<Dream> dreams = Dream.listAll(Dream.class);
            // create some random data
            int number = (new Random().nextInt(dreams.size()));
            //String number = "Nama Saya OM";
            //String[] omom = {"Nama Saya Om", "Om nama saya", "Bukan Om Om"};
            String[] dreamSet = new String[dreams.size()];
            int inc = 0;
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.widget_layout);

            for(Dream om : dreams){
                dreamSet[inc] = om.getName().toString();
                TextView tv = new TextView(context);
                tv.setText(om.getName().toString());

                Log.w("WidgetExample", String.valueOf(dreamSet[number]));
                // Set the textz
                remoteViews.setTextViewText(R.id.update, dreamSet[number]);
                inc++;
            }


            // Register an onClickListener
            Intent intent = new Intent(context, DreamyWidgetProvider.class);

            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.update, pendingIntent);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }
}
