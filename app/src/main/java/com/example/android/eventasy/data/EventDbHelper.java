package com.example.android.eventasy.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.android.eventasy.data.EventContract.EventEntry;

/**
 * Created by kakalra on 1/7/2017.
 */

public class EventDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION=1;

    static final String DATABASE_NAME="event.db";

    public EventDbHelper(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_EVENT_TABLE="CREATE TABLE "+ EventEntry.TABLE_NAME+" ("+
                EventEntry._ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                EventEntry.COLUMN_EVENT_ID+" TEXT,"+
                EventEntry.COLUMN_EVENT_TITLE+" TEXT,"+
                EventEntry.COLUMN_EVENT_START_TIME+" TEXT,"+
                EventEntry.COLUMN_EVENT_VENUE_NAME+" TEXT,"+
                EventEntry.COLUMN_EVENT_IMAGE_BLOCK200_URL+" TEXT,"+
                EventEntry.COLUMN_EVENT_IMAGE_LARGE_URL+" TEXT"+
                ");";

        db.execSQL(SQL_CREATE_EVENT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+EventEntry.TABLE_NAME);
        onCreate(db);
    }
}
