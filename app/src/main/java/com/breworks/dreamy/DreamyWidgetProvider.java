package com.breworks.dreamy;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * Created by R A W on 11/27/2014.
 */

public class DreamyWidgetProvider extends AppWidgetProvider {
    private static final String ACTION_CLICK = "ACTION_CLICK";
    private static final String SYNC_CLICKED = "automaticWidgetSyncButtonClick";
    int [] appWidget = null;
    AppWidgetManager appWidgetManager;

    //SessionManager session;
    //dreamyAccount login;
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        appWidget = appWidgetIds;
        this.appWidgetManager = appWidgetManager;
        Log.e("MASUK WIDGET",SYNC_CLICKED);
        final int N = appWidgetIds.length;

		/*int[] appWidgetIds holds ids of multiple instance of your widget
		 * meaning you are placing more than one widgets on your homescreen*/
        for (int i = 0; i < N; ++i) {
            RemoteViews remoteViews = updateWidgetListView(context,
                    appWidgetIds[i]);
            Intent startActivityIntent = new Intent(context, Splash.class);
            PendingIntent startActivityPendingIntent = PendingIntent.getActivity(context, 0, startActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setPendingIntentTemplate(R.id.listViewWidget, startActivityPendingIntent);

            remoteViews.setOnClickPendingIntent(R.id.refresh_button, getPendingSelfIntent(context, SYNC_CLICKED));

            appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (SYNC_CLICKED.equals(intent.getAction())) {
            Log.e("BREWORKS", String.valueOf(appWidget.length));
//            final int N = appWidget.length;
//            for (int i = 0; i < N; ++i) {
//                RemoteViews remoteViews = updateWidgetListView(context,
//                        appWidget[i]);
//                Intent startActivityIntent = new Intent(context, Splash.class);
//                PendingIntent startActivityPendingIntent = PendingIntent.getActivity(context, 0, startActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//                remoteViews.setPendingIntentTemplate(R.id.listViewWidget, startActivityPendingIntent);
//
//                remoteViews.setOnClickPendingIntent(R.id.refresh_button, getPendingSelfIntent(context, SYNC_CLICKED));
//
//                appWidgetManager.updateAppWidget(appWidget[i], remoteViews);
//            }
             }
        }

    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    private RemoteViews updateWidgetListView(Context context, int appWidgetId) {

        //which layout to show on widget
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.widget_layout);


        //RemoteViews Service needed to provide adapter for ListView
        Intent svcIntent = new Intent(context, WidgetService.class);
        //passing app widget id to that RemoteViews Service
        svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        //setting a unique Uri to the intent
        //don't know its purpose to me right now
        svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));
        //setting adapter to listview of the widget
        remoteViews.setRemoteAdapter(appWidgetId, R.id.listViewWidget,
                svcIntent);
        //setting an empty view in case of no data
        remoteViews.setEmptyView(R.id.listViewWidget, R.id.empty_view);
        return remoteViews;
    }
    }
