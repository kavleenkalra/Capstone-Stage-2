package com.example.android.eventasy.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.eventasy.BuildConfig;
import com.example.android.eventasy.ImportantConstants;
import com.example.android.eventasy.R;
import com.example.android.eventasy.Utility;
import com.example.android.eventasy.adapters.PagerAdapter;
import com.example.android.eventasy.data.EventContract;
import com.example.android.eventasy.retrofit.model.Event;
import com.example.android.eventasy.retrofit.rest.ApiClient;
import com.example.android.eventasy.retrofit.rest.ApiInterface;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventDetailActivity extends AppCompatActivity {

    private final String LOG_TAG=EventDetailActivity.class.getSimpleName();

    Event event;
    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;
    NestedScrollView nestedScrollView;
    FloatingActionButton fab;
    TabLayout tabLayout;
    ViewPager viewPager;
    TextView eventTitleTextView;
    String eventId;
    String eventTitle;

    private ShareActionProvider mShareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState==null)
        {
            int activityCode=getIntent().getExtras().getInt("CallingActivity");
            if(activityCode== ImportantConstants.EVENT_LIST_ACTIVITY || activityCode==ImportantConstants.MAIN_ACTIVITY || activityCode==ImportantConstants.SEARCH_ACTIVITY){
                event=getIntent().getExtras().getParcelable("EventDetailObject");
                eventId=event.getEventId();
                eventTitle=event.getEventTitle();
            }
            else if(getIntent().getExtras().getInt("CallingActivity")==ImportantConstants.FAVOURITE_ACTIVITY){
                eventId=getIntent().getExtras().getString("EventId");
                eventTitle=getIntent().getExtras().getString("EventTitle");
                fetchEventDetail(eventId);
            }
        }
        else{
            event=savedInstanceState.getParcelable("EventDetail");
            eventId=event.getEventId();
            eventTitle=event.getEventTitle();
        }
        setContentView(R.layout.activity_event_detail);

        AdView adView=(AdView)findViewById(R.id.adView);
        AdRequest adRequest=new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        collapsingToolbarLayout=(CollapsingToolbarLayout)findViewById(R.id.event_detail_collapsing_toolbar);
        collapsingToolbarLayout.setTitle(getString(R.string.app_name));
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.transparent));
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsed_appbar_title_style);

        ImageView collapsingImageView=(ImageView)collapsingToolbarLayout.findViewById(R.id.event_detail_collapsing_image);

        toolbar=(Toolbar)collapsingToolbarLayout.findViewById(R.id.event_detail_toolbar);
        if(toolbar!=null){
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeActionContentDescription(R.string.go_back_previous_screen);
        }

        nestedScrollView=(NestedScrollView)findViewById(R.id.event_detail_scroll_view);

        eventTitleTextView=(TextView)nestedScrollView.findViewById(R.id.event_detail_title_view);
        viewPager=(ViewPager)nestedScrollView.findViewById(R.id.event_detail_view_pager);

        tabLayout=(TabLayout)nestedScrollView.findViewById(R.id.event_detail_tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        fab=(FloatingActionButton)findViewById(R.id.location_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(event!=null) {
                    if (event.getEventLongitude() != null && event.getEventLongitude() != null && event.getEventVenueName() != null) {
                        setMapIntent();
                    }
                }
            }
        });

        int callingActivityCode=getIntent().getExtras().getInt("CallingActivity");
        if(callingActivityCode==ImportantConstants.EVENT_LIST_ACTIVITY || callingActivityCode==ImportantConstants.MAIN_ACTIVITY ||callingActivityCode==ImportantConstants.SEARCH_ACTIVITY){
            if(event.getEventImage()!=null && event.getEventImage().getLargeImage()!=null){
                Picasso.with(getApplicationContext())
                        .load((event.getEventImage()).getLargeImage().getImageUrl())
                        .placeholder(R.drawable.main_screen_image)
                        .error(R.drawable.error_image)
                        .into(collapsingImageView);

            }
            setupViewPager();
        }
        else{
            Picasso.with(getApplicationContext())
                    .load(getIntent().getExtras().getString("EventImageUrl"))
                    .placeholder(R.drawable.main_screen_image)
                    .error(R.drawable.error_image)
                    .into(collapsingImageView);
            if(event!=null)
                setupViewPager();
        }
        eventTitleTextView.setText(eventTitle);
    }

    private void setupViewPager(){
        PagerAdapter adapter=new PagerAdapter(getSupportFragmentManager());
        if(event!=null){
            Bundle args=new Bundle();
            args.putParcelable("EventDetailObject",event);

            DetailsTabFragment detailsTabFragment=new DetailsTabFragment();
            detailsTabFragment.setArguments(args);
            DescriptionTabFragment descriptionTabFragment=new DescriptionTabFragment();
            descriptionTabFragment.setArguments(args);

            adapter.addFragment(detailsTabFragment,"Details");
            adapter.addFragment(descriptionTabFragment,"Description");

            viewPager.setAdapter(adapter);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("EventDetail",event);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater=getMenuInflater();

        inflater.inflate(R.menu.menu_event_detail,menu);
        final MenuItem favourite=menu.findItem(R.id.favourite_item);

        new AsyncTask<Void,Void,Integer>(){

            @Override
            protected Integer doInBackground(Void... params) {
                return Utility.isEventSetToFavourite(getApplicationContext(),eventId);
            }

            @Override
            protected void onPostExecute(Integer integer) {
                if(integer==1){
                    favourite.setIcon(R.drawable.ic_favorite_red_36dp);
                }
                else
                    favourite.setIcon(R.drawable.ic_favorite_border_white_36dp);
                }
            }.execute();

        MenuItem share=menu.findItem(R.id.share_item);
        mShareActionProvider=(ShareActionProvider) MenuItemCompat.getActionProvider(share);
        if(mShareActionProvider!=null)
            mShareActionProvider.setShareIntent(createShareEventDetailIntent(eventId));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        switch (item.getItemId()){

            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.favourite_item:
                if(event!=null){
                    new AsyncTask<Void,Void,Integer>(){

                        @Override
                        protected Integer doInBackground(Void... voids) {
                            return Utility.isEventSetToFavourite(getApplicationContext(),event.getEventId());
                        }

                        @Override
                        protected void onPostExecute(Integer integer) {
                            if(integer==1){

                                //event is already in favourites. Delete it from favourites.
                                new AsyncTask<Void,Void,Integer>(){
                                    @Override
                                    protected Integer doInBackground(Void... voids) {
                                        int rowsDeleted=getApplicationContext().getContentResolver().delete(
                                                EventContract.EventEntry.CONTENT_URI,
                                                EventContract.EventEntry.COLUMN_EVENT_ID+" = ?",
                                                new String[]{event.getEventId()}
                                        );
                                        return rowsDeleted;
                                    }

                                    @Override
                                    protected void onPostExecute(Integer integer) {
                                        item.setIcon(R.drawable.ic_favorite_border_white_36dp);
                                        Toast toast=Toast.makeText(getApplicationContext(),R.string.favourite_removed,Toast.LENGTH_SHORT);
                                        toast.show();
                                    }
                                }.execute();
                            }
                            else
                            {
                                //event has to be added to favourites.
                                new AsyncTask<Void,Void,Uri>(){
                                    @Override
                                    protected Uri doInBackground(Void... params) {

                                        ContentValues contentValues=new ContentValues();

                                        contentValues.put(EventContract.EventEntry.COLUMN_EVENT_ID,event.getEventId());
                                        contentValues.put(EventContract.EventEntry.COLUMN_EVENT_TITLE,event.getEventTitle());
                                        contentValues.put(EventContract.EventEntry.COLUMN_EVENT_START_TIME,event.getEventStartTime());
                                        contentValues.put(EventContract.EventEntry.COLUMN_EVENT_VENUE_NAME,event.getEventVenueName());
                                        contentValues.put(EventContract.EventEntry.COLUMN_EVENT_IMAGE_BLOCK200_URL,event.getEventImage().getBlock200Image().getImageUrl());
                                        contentValues.put(EventContract.EventEntry.COLUMN_EVENT_IMAGE_LARGE_URL,event.getEventImage().getLargeImage().getImageUrl());

                                        return getApplicationContext().getContentResolver().insert(EventContract.EventEntry.CONTENT_URI,contentValues);
                                    }

                                    @Override
                                    protected void onPostExecute(Uri uri) {
                                        item.setIcon(R.drawable.ic_favorite_red_36dp);
                                        Toast toast=Toast.makeText(getApplicationContext(),R.string.favourite_added,Toast.LENGTH_SHORT);
                                        toast.show();
                                    }
                                }.execute();
                            }
                        }
                    }.execute();
                    Utility.updateWidgets(getApplicationContext());
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private Intent createShareEventDetailIntent(String id){
        Intent shareIntent=new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,eventTitle+" : "+"http://eventful.com/events/"+id);
        return shareIntent;
    }

    private void fetchEventDetail(final String eventId){
        ApiInterface apiInterface= ApiClient.getClient().create(ApiInterface.class);
        Call<Event> call=apiInterface.getEventDetail(eventId, BuildConfig.EVENT_API_KEY);
        call.enqueue(new Callback<Event>() {
            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {
                if(response.isSuccessful()){
                    event=new Event();
                    setEventAttributes(response);
                    setupViewPager();
                }
            }

            @Override
            public void onFailure(Call<Event> call, Throwable t) {
                Log.e(LOG_TAG,"Failed to fetch Event Detail "+t.toString());
            }
        });
    }

    private void setEventAttributes(Response<Event> response){
        event.setEventId(response.body().getEventId());
        event.setEventUrl(response.body().getEventUrl());
        event.setEventTitle(response.body().getEventTitle());
        event.setEventDescription(response.body().getEventDescription());
        event.setEventStartTime(response.body().getEventStartTime());
        event.setEventStopTime(response.body().getEventStopTime());
        event.setEventVenueName(response.body().getEventVenueName());
        event.setEventAddress(response.body().getEventAddress());
        event.setEventCity(response.body().getEventCity());
        event.setEventRegion(response.body().getEventRegion());
        event.setEventPostalCode(response.body().getEventPostalCode());
        event.setEventCountry(response.body().getEventCountry());
        event.setEventLatitude(response.body().getEventLatitude());
        event.setEventLongitude(response.body().getEventLongitude());
        event.setEventPrice(response.body().getEventPrice());
    }

    private void setMapIntent(){
        double latitude = event.getEventLatitude();
        double longitude = event.getEventLongitude();
        String label = event.getEventVenueName();
        String uriBegin = "geo:" + latitude + "," + longitude;
        String query = latitude + "," + longitude + "(" + label + ")";
        String encodedQuery = Uri.encode(query);
        String uriString = uriBegin + "?q=" + encodedQuery + "&z=15.0";
        Uri gmmIntentUri = Uri.parse(uriString);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null)
            startActivity(mapIntent);
        else
            Toast.makeText(getApplicationContext(), getString(R.string.map_intent_message), Toast.LENGTH_SHORT).show();
    }
}
