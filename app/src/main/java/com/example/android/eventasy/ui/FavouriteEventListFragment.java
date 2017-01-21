package com.example.android.eventasy.ui;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.eventasy.ImportantConstants;
import com.example.android.eventasy.R;
import com.example.android.eventasy.adapters.FavouriteEventCursorAdapter;
import com.example.android.eventasy.data.EventContract;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavouriteEventListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    ListView favouriteEventListView;
    FavouriteEventCursorAdapter adapter;

    public FavouriteEventListFragment() {
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_favourite_event_list, container, false);
        favouriteEventListView=(ListView)rootView.findViewById(R.id.favourite_list_view);

        Cursor cursor=getActivity().getContentResolver().query(EventContract.EventEntry.CONTENT_URI,null,null,null,null);
        adapter=new FavouriteEventCursorAdapter(getContext(),cursor);
        favouriteEventListView.setAdapter(adapter);

        favouriteEventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor c=adapter.getCursor();
                c.moveToPosition(i);
                Intent intent=new Intent(getActivity(),EventDetailActivity.class);
                intent.putExtra("CallingActivity", ImportantConstants.FAVOURITE_ACTIVITY);
                intent.putExtra("EventId",c.getString(c.getColumnIndexOrThrow("event_id")));
                intent.putExtra("EventImageUrl",c.getString(c.getColumnIndexOrThrow("event_large_image")));
                intent.putExtra("EventTitle",c.getString(c.getColumnIndexOrThrow("event_title")));
                startActivity(intent);
            }
        });

        getActivity().getSupportLoaderManager().initLoader(0,null,this);
        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri= EventContract.EventEntry.CONTENT_URI;
        return new CursorLoader(getActivity(),uri,null,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(adapter!=null && data!=null)
            adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if(adapter!=null)
            adapter.swapCursor(null);
    }

}
