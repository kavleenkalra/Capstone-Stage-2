package com.example.android.eventasy.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.TaskStackBuilder;
import android.widget.RemoteViews;

import com.example.android.eventasy.R;
import com.example.android.eventasy.ui.EventDetailActivity;
import com.example.android.eventasy.ui.FavouriteEventListActivity;

/**
 * Created by kakalra on 1/16/2017.
 */

public class EventWidgetProvider extends AppWidgetProvider{

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int i=0;i<appWidgetIds.length;i++){
            RemoteViews views=new RemoteViews(context.getPackageName(),R.layout.widget_event_detail);

            //Create an intent to launch main activity.
            Intent intent=new Intent(context, FavouriteEventListActivity.class);
            PendingIntent pendingIntent=PendingIntent.getActivity(context,0,intent,0);
            views.setOnClickPendingIntent(R.id.widget,pendingIntent);

            //Set up the collection
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                setRemoteAdapter(context, views);
            } else {
                setRemoteAdapterV11(context, views);
            }

            Intent detailActivityIntent=new Intent(context, EventDetailActivity.class);
            PendingIntent clickPendingIntentTemplate= TaskStackBuilder.create(context).
                    addNextIntentWithParentStack(detailActivityIntent).getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.widget_list,clickPendingIntentTemplate);
            views.setEmptyView(R.id.widget_list,R.id.widget_empty);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetIds[i],views);
        }
    }

    @Override
    public void onReceive(@NonNull Context context,@NonNull Intent intent){
        super.onReceive(context, intent);
        if(intent.getAction().equalsIgnoreCase("com.example.android.eventasy.ACTION_DATA_UPDATED")){
            AppWidgetManager appWidgetManager=AppWidgetManager.getInstance(context);
            int[] appWidgetIds=appWidgetManager.getAppWidgetIds(new ComponentName(context,getClass()));
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds,R.id.widget_list);
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void setRemoteAdapter(Context context, @NonNull final RemoteViews views)
    {
        views.setRemoteAdapter(R.id.widget_list,
                new Intent(context,EventWidgetRemoteViewsService.class));
    }

    @SuppressWarnings("deprecation")
    private void setRemoteAdapterV11(Context context, @NonNull final RemoteViews views)
    {
        views.setRemoteAdapter(0, R.id.widget_list,
                new Intent(context,EventWidgetRemoteViewsService.class));
    }
}
