package com.example.android.eventasy.ui;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.eventasy.BuildConfig;
import com.example.android.eventasy.ImportantConstants;
import com.example.android.eventasy.ItemClickListener;
import com.example.android.eventasy.R;
import com.example.android.eventasy.Utility;
import com.example.android.eventasy.adapters.UpcomingEventsAdapter;
import com.example.android.eventasy.retrofit.model.Event;
import com.example.android.eventasy.retrofit.model.EventList;
import com.example.android.eventasy.retrofit.model.EventResponse;
import com.example.android.eventasy.retrofit.rest.ApiClient;
import com.example.android.eventasy.retrofit.rest.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;
import static com.example.android.eventasy.ImportantConstants.IMAGE_SIZE;
import static com.example.android.eventasy.ImportantConstants.SORT_DIRECTION;
import static com.example.android.eventasy.ImportantConstants.SORT_ORDER;
import static com.example.android.eventasy.ImportantConstants.UPCOMING_EVENT_PAGE_SIZE;

/**
 * A placeholder fragment containing a simple view.
 */

public class MainActivityFragment extends Fragment implements ItemClickListener {

    private final String LOG_TAG=MainActivityFragment.class.getSimpleName();

    RecyclerView upcomingEventsView;
    ProgressBar spinner;
    TextView emptyView;
    View rootView;
    ImageButton imageButton;

    UpcomingEventsAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    EventList eventList;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState==null || !savedInstanceState.containsKey("Events")){
            eventList=new EventList();
        }
        else{
            eventList=savedInstanceState.getParcelable("Events");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if(eventList.getEventList().size()==0)
        {
            if(Utility.isNetworkAvailable(getContext())){
                eventList=fetchUpcomingEvents();
            }
            else
            {
                upcomingEventsView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
                emptyView.setText(getString(R.string.no_network_available));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        //initialising views.
        rootView=inflater.inflate(R.layout.fragment_main, container, false);
        imageButton=(ImageButton)rootView.findViewById(R.id.search_button);
        upcomingEventsView=(RecyclerView)rootView.findViewById(R.id.upcoming_events_recycler_view);
        spinner=(ProgressBar)rootView.findViewById(R.id.upcoming_event_list_progress_bar);
        emptyView=(TextView)rootView.findViewById(R.id.empty_upcoming_event_view);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),SearchInputActivity.class);
                startActivity(intent);
            }
        });

        linearLayoutManager=new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        upcomingEventsView.setHasFixedSize(true);
        upcomingEventsView.setLayoutManager(linearLayoutManager);

        adapter=new UpcomingEventsAdapter(getContext(),eventList);
        upcomingEventsView.setAdapter(adapter);
        adapter.setClickListener(this);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("Events",eventList);
        super.onSaveInstanceState(outState);
    }

    public EventList fetchUpcomingEvents(){
        setViewsVisibility();
        ApiInterface apiInterface= ApiClient.getClient().create(ApiInterface.class);
        Call<EventResponse> call=apiInterface.getUpcomingEventList(Utility.getDateRangeString(20),SORT_ORDER,SORT_DIRECTION,UPCOMING_EVENT_PAGE_SIZE,IMAGE_SIZE,BuildConfig.EVENT_API_KEY);
        call.enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                if(response.isSuccessful()){
                    Activity activity=getActivity();
                    if(activity!=null && isAdded()){
                        eventList.setEventList(response.body().getAllEvents().getEventList());
                        if(adapter!=null){
                            adapter.notifyDataSetChanged();
                            spinner.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<EventResponse> call, Throwable t) {
                Activity activity=getActivity();
                if(activity!=null && isAdded()){
                    spinner.setVisibility(View.GONE);
                    upcomingEventsView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                    if(emptyView!=null)
                        emptyView.setText(getString(R.string.error_fetching_data));
                    Log.e(LOG_TAG,"Fetching upcoming events failed "+t.toString());
                }
            }
        });
        return eventList;
    }

    @Override
    public void onClick(View view, int position) {
        Event event=eventList.getEventList().get(position);
        Intent intent=new Intent(getActivity(),EventDetailActivity.class);
        intent.putExtra("CallingActivity", ImportantConstants.MAIN_ACTIVITY);
        intent.putExtra("EventDetailObject",event);
        startActivity(intent);
    }

    private void setViewsVisibility(){
        if(spinner.getVisibility()== GONE || spinner.getVisibility()==View.INVISIBLE)
            spinner.setVisibility(View.VISIBLE);

        if(emptyView.getVisibility()==View.VISIBLE)
            emptyView.setVisibility(GONE);

        if(upcomingEventsView.getVisibility()==GONE || upcomingEventsView.getVisibility()==View.INVISIBLE)
            upcomingEventsView.setVisibility(View.VISIBLE);
    }
}
