package com.example.android.eventasy.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by kakalra on 1/7/2017.
 */

public class EventContract {

    public static final String CONTENT_AUTHORITY="com.example.android.eventasy";
    public static final Uri BASE_CONTENT_URI= Uri.parse("content://"+CONTENT_AUTHORITY);
    public static final String PATH_EVENT="event";

    public static final class EventEntry implements BaseColumns{

        public static final Uri CONTENT_URI=BASE_CONTENT_URI.buildUpon().appendPath(PATH_EVENT).build();

        public static final String CONTENT_TYPE=
                ContentResolver.CURSOR_DIR_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+PATH_EVENT;

        public static final String CONTENT_ITEM_TYPE=
                ContentResolver.CURSOR_ITEM_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+PATH_EVENT;

        public static final String TABLE_NAME="event";

        public static final String COLUMN_EVENT_ID="event_id";
        public static final String COLUMN_EVENT_TITLE="event_title";
        public static final String COLUMN_EVENT_START_TIME="event_start_time";
        public static final String COLUMN_EVENT_VENUE_NAME="event_venue_name";
        public static final String COLUMN_EVENT_IMAGE_BLOCK200_URL="event_block200_image";
        public static final String COLUMN_EVENT_IMAGE_LARGE_URL="event_large_image";

        public static Uri buildEventUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }
    }
}
