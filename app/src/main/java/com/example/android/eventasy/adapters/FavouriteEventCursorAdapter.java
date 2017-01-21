package com.example.android.eventasy.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.eventasy.R;
import com.example.android.eventasy.Utility;
import com.squareup.picasso.Picasso;

/**
 * Created by kakalra on 1/10/2017.
 */

public class FavouriteEventCursorAdapter extends CursorAdapter {

    public FavouriteEventCursorAdapter(Context context, Cursor cursor){
        super(context,cursor,0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.favourite_event_item,viewGroup,false);
    }

    @Override
    public void bindView(View view, Context context, final Cursor cursor) {

        ImageView eventListImageView=(ImageView)view.findViewById(R.id.favourite_event_image_view);
        TextView eventTitleTextView=(TextView)view.findViewById(R.id.favourite_event_title_view);
        TextView eventDateTimeTextView=(TextView)view.findViewById(R.id.favourite_event_date_time_venue_view);

        Picasso.with(context)
                .load(cursor.getString(cursor.getColumnIndexOrThrow("event_block200_image")))
                .placeholder(R.drawable.main_screen_image)
                .error(R.drawable.error_image)
                .into(eventListImageView);

        eventTitleTextView.setText(cursor.getString(cursor.getColumnIndexOrThrow("event_title")));
        eventDateTimeTextView.setText(Utility.getFormattedDate(cursor.getString(cursor.getColumnIndexOrThrow("event_start_time")))+" at "+cursor.getString(cursor.getColumnIndexOrThrow("event_venue_name")));
    }
}
