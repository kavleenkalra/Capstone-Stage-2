package com.example.android.eventasy.widget;

import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.eventasy.ImportantConstants;
import com.example.android.eventasy.R;
import com.example.android.eventasy.Utility;
import com.example.android.eventasy.data.EventContract;

/**
 * Created by kakalra on 1/16/2017.
 */

public class EventWidgetRemoteViewsService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        return new RemoteViewsFactory() {

            private Cursor data=null;

            @Override
            public void onCreate() {

            }

            @Override
            public void onDataSetChanged() {

                if (data!=null)
                    data.close();
                final long identityToken= Binder.clearCallingIdentity();
                data=getContentResolver().query(EventContract.EventEntry.CONTENT_URI,
                        new String[]{
                            EventContract.EventEntry._ID,
                            EventContract.EventEntry.COLUMN_EVENT_ID,
                            EventContract.EventEntry.COLUMN_EVENT_TITLE,
                            EventContract.EventEntry.COLUMN_EVENT_START_TIME,
                            EventContract.EventEntry.COLUMN_EVENT_VENUE_NAME,
                            EventContract.EventEntry.COLUMN_EVENT_IMAGE_BLOCK200_URL,
                            EventContract.EventEntry.COLUMN_EVENT_IMAGE_LARGE_URL
                        },
                        null,
                        null,
                        null);
                Binder.restoreCallingIdentity(identityToken);
            }

            @Override
            public void onDestroy() {
                if(data!=null){
                    data.close();
                    data=null;
                }
            }

            @Override
            public int getCount() {
                return data==null?0:data.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                if(position== AdapterView.INVALID_POSITION || data==null || !data.moveToPosition(position))
                {
                    return null;
                }
                RemoteViews views=new RemoteViews(getPackageName(),R.layout.widget_event_list_item_detail);
                String eventTitle=data.getString(2);
                String eventDate=data.getString(3);

                views.setTextViewText(R.id.widget_event_title,eventTitle);
                views.setContentDescription(R.id.widget_event_title,getApplicationContext().getString(R.string.talk_back_event_name)+eventTitle);

                views.setTextViewText(R.id.widget_event_date,eventDate);
                views.setContentDescription(R.id.widget_event_date,getApplicationContext().getString(R.string.talk_back_event_date)+ Utility.getFormattedDate(eventDate));

                Bundle extras=new Bundle();
                extras.putInt("CallingActivity", ImportantConstants.FAVOURITE_ACTIVITY);
                extras.putString("EventId",data.getString(1));
                extras.putString("EventImageUrl",data.getString(6));
                extras.putString("EventTitle",data.getString(2));

                final Intent fillInIntent=new Intent();
                fillInIntent.putExtras(extras);
                views.setOnClickFillInIntent(R.id.widget_list_item,fillInIntent);

                return views;
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.widget_event_list_item_detail);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                if(data!=null && data.moveToPosition(position))
                {
                    return data.getLong(0);
                }
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }
}
