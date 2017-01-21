package com.example.android.eventasy.ui;

import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.eventasy.BuildConfig;
import com.example.android.eventasy.ImportantConstants;
import com.example.android.eventasy.ItemClickListener;
import com.example.android.eventasy.R;
import com.example.android.eventasy.Utility;
import com.example.android.eventasy.adapters.EventListAdapter;
import com.example.android.eventasy.retrofit.model.Event;
import com.example.android.eventasy.retrofit.model.EventResponse;
import com.example.android.eventasy.retrofit.rest.ApiClient;
import com.example.android.eventasy.retrofit.rest.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;
import static com.example.android.eventasy.ImportantConstants.LANDSCAPE_COLUMNS;
import static com.example.android.eventasy.ImportantConstants.PAGE_SIZE;
import static com.example.android.eventasy.ImportantConstants.PORTRAIT_COLUMNS;

public class SearchableActivity extends AppCompatActivity implements ItemClickListener{

    private final String LOG_TAG=SearchableActivity.class.getSimpleName();

    RecyclerView searchListView;
    TextView emptyView;
    ProgressBar spinner;
    EventListAdapter adapter;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    EventResponse eventResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);
        Toolbar toolbar=(Toolbar)findViewById(R.id.searchable_toolbar);
        if(toolbar!=null){
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeActionContentDescription(R.string.go_back_main_screen);
        }

        searchListView=(RecyclerView)findViewById(R.id.search_recycler_view);
        searchListView.setHasFixedSize(true);

        int noOfColumns=getResources().getConfiguration().orientation== Configuration.ORIENTATION_PORTRAIT?PORTRAIT_COLUMNS:LANDSCAPE_COLUMNS;
        staggeredGridLayoutManager=new StaggeredGridLayoutManager(noOfColumns,StaggeredGridLayoutManager.VERTICAL);
        searchListView.setLayoutManager(staggeredGridLayoutManager);

        emptyView=(TextView)findViewById(R.id.empty_search_list_view);
        spinner=(ProgressBar)findViewById(R.id.search_list_progress_bar);

        if(savedInstanceState==null || !savedInstanceState.containsKey("EventList")){
            eventResponse=new EventResponse();
            handleIntent(getIntent());
        }
        else{
            eventResponse=savedInstanceState.getParcelable("EventList");
        }

        adapter=new EventListAdapter(getApplicationContext(),eventResponse);
        searchListView.setAdapter(adapter);
        adapter.setClickListener(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent){
        if(intent.ACTION_SEARCH.equals(intent.getAction())){
            String query=intent.getStringExtra(SearchManager.QUERY);
            query.replaceAll("\\s","+");
            fetchSearchResults(query);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("EventList",eventResponse);
        super.onSaveInstanceState(outState);
    }

    private EventResponse fetchSearchResults(String query){

        setViewsVisibility();
        ApiInterface apiInterface= ApiClient.getClient().create(ApiInterface.class);

        Call<EventResponse> call=apiInterface.getSearchResults(query, Utility.getDateRangeString(120),PAGE_SIZE,ImportantConstants.IMAGE_SIZE,BuildConfig.EVENT_API_KEY);
        call.enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                if(response.isSuccessful()){
                    setEventAttributes(response);
                    if(adapter!=null){
                        adapter.notifyDataSetChanged();
                        spinner.setVisibility(GONE);
                        searchListView.setVisibility(View.VISIBLE);

                        //checking if no of items fetched are 0.
                        if(response.body().getTotalItems()==0)
                        {
                            searchListView.setVisibility(GONE);
                            emptyView.setVisibility(View.VISIBLE);
                            emptyView.setText(R.string.no_results_found);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<EventResponse> call, Throwable t) {
                spinner.setVisibility(GONE);
                searchListView.setVisibility(GONE);
                emptyView.setVisibility(View.VISIBLE);
                emptyView.setText(getString(R.string.error_fetching_data));
                Log.e(LOG_TAG,"Failed to fetch Search Results"+t.toString());
            }
        });
        return eventResponse;
    }

    private void setEventAttributes(Response<EventResponse> response){
        eventResponse.setAllEvents(response.body().getAllEvents());
        eventResponse.setPageNumber(response.body().getPageNumber());
        eventResponse.setPageSize(response.body().getPageSize());
        eventResponse.setTotalItems(response.body().getTotalItems());
    }

    @Override
    public void onClick(View view, int position) {
        Event event=eventResponse.getAllEvents().getEventList().get(position);
        Intent intent=new Intent(getApplicationContext(),EventDetailActivity.class);
        intent.putExtra("CallingActivity", ImportantConstants.SEARCH_ACTIVITY);
        intent.putExtra("EventDetailObject",event);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }

    private void setViewsVisibility(){
        if(spinner.getVisibility()== GONE || spinner.getVisibility()==View.INVISIBLE)
            spinner.setVisibility(View.VISIBLE);

        if(emptyView.getVisibility()==View.VISIBLE)
            emptyView.setVisibility(GONE);

        if(searchListView.getVisibility()==GONE || searchListView.getVisibility()==View.INVISIBLE)
            searchListView.setVisibility(View.VISIBLE);
    }
}
