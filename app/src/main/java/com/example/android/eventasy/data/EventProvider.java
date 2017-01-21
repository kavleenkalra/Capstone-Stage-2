package com.example.android.eventasy.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by kakalra on 1/7/2017.
 */

public class EventProvider extends ContentProvider{

    private static final UriMatcher sUriMatcher=buildUriMatcher();
    private EventDbHelper mOpenHelper;

    static final int EVENT=100;

    static UriMatcher buildUriMatcher()
    {
        final UriMatcher matcher=new UriMatcher(UriMatcher.NO_MATCH);
        final String authority=EventContract.CONTENT_AUTHORITY;

        matcher.addURI(authority,EventContract.PATH_EVENT,EVENT);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper=new EventDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor retCursor;
        switch (sUriMatcher.match(uri))
        {
            case EVENT:
            {
                retCursor=mOpenHelper.getReadableDatabase().query(
                        EventContract.EventEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown Uri:"+uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(),uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match=sUriMatcher.match(uri);
        switch (match)
        {
            case EVENT:
                return EventContract.EventEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown Uri:"+uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db=mOpenHelper.getWritableDatabase();
        final int match=sUriMatcher.match(uri);
        Uri returnUri;

        switch (match)
        {
            case EVENT:
                long _id=db.insert(EventContract.EventEntry.TABLE_NAME,null,values);
                if(_id>0)
                    returnUri=EventContract.EventEntry.buildEventUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into "+ uri);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri:"+uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return returnUri;
    }


    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db=mOpenHelper.getWritableDatabase();
        final int match=sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match)
        {
            case EVENT:
                rowsUpdated=db.update(EventContract.EventEntry.TABLE_NAME,values,selection,selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri:"+uri);
        }
        if(rowsUpdated!=0)
        {
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return rowsUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db=mOpenHelper.getWritableDatabase();
        final int match=sUriMatcher.match(uri);
        int rowsDeleted;

        // this makes delete all rows return the number of rows deleted
        if (null==selection)
            selection="1";

        switch (match)
        {
            case EVENT:
                rowsDeleted=db.delete(EventContract.EventEntry.TABLE_NAME,selection,selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri:"+uri);
        }

        // Because a null deletes all rows
        if (rowsDeleted!=0)
        {
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return rowsDeleted;
    }
}
