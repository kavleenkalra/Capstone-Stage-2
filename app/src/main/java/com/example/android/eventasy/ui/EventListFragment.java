package com.example.android.eventasy.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.eventasy.BuildConfig;
import com.example.android.eventasy.ImportantConstants;
import com.example.android.eventasy.ItemClickListener;
import com.example.android.eventasy.R;
import com.example.android.eventasy.Utility;
import com.example.android.eventasy.adapters.EventListAdapter;
import com.example.android.eventasy.retrofit.model.Category;
import com.example.android.eventasy.retrofit.model.Event;
import com.example.android.eventasy.retrofit.model.EventResponse;
import com.example.android.eventasy.retrofit.rest.ApiClient;
import com.example.android.eventasy.retrofit.rest.ApiInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;
import static com.example.android.eventasy.ImportantConstants.IMAGE_SIZE;
import static com.example.android.eventasy.ImportantConstants.LANDSCAPE_COLUMNS;
import static com.example.android.eventasy.ImportantConstants.PAGE_SIZE;
import static com.example.android.eventasy.ImportantConstants.PORTRAIT_COLUMNS;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventListFragment extends Fragment implements ItemClickListener,SharedPreferences.OnSharedPreferenceChangeListener{

    private final String LOG_TAG=EventListFragment.class.getSimpleName();

    RecyclerView eventListStaggeredView;
    TextView emptyView;
    ProgressBar spinner;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    EventListAdapter eventListAdapter;
    EventResponse eventResponse;
    Category categoryObj;
    SharedPreferences sharedPreferences;
    String location;
    String sortType;
    int searchRadius;

    public EventListFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState==null || !savedInstanceState.containsKey("EventList")){
            eventResponse=new EventResponse();
        }
        else{
            eventResponse=savedInstanceState.getParcelable("EventList");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if(eventResponse.getAllEvents().getEventList().size()==0){
            if(Utility.isNetworkAvailable(getContext())){
                if(categoryObj!=null){
                    eventResponse=fetchEventList(categoryObj.getCategoryId());
                }
            }
            else {
                eventListStaggeredView.setVisibility(GONE);
                emptyView.setVisibility(View.VISIBLE);
                emptyView.setText(getString(R.string.no_network_available));
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("EventList",eventResponse);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        Bundle arguments=getArguments();
        if(arguments!=null)
        {
            categoryObj=arguments.getParcelable("CategoryObject");
        }

        View rootView=inflater.inflate(R.layout.fragment_event_list, container, false);
        eventListStaggeredView=(RecyclerView)rootView.findViewById(R.id.event_list_recycler_view);
        eventListStaggeredView.setHasFixedSize(true);
        int noOfColumns=getActivity().getResources().getConfiguration().orientation== Configuration.ORIENTATION_PORTRAIT?PORTRAIT_COLUMNS:LANDSCAPE_COLUMNS;

        staggeredGridLayoutManager=new StaggeredGridLayoutManager(noOfColumns,StaggeredGridLayoutManager.VERTICAL);
        eventListStaggeredView.setLayoutManager(staggeredGridLayoutManager);

        emptyView=(TextView)rootView.findViewById(R.id.empty_event_list_view);
        spinner=(ProgressBar)rootView.findViewById(R.id.event_list_progress_bar);

        eventListAdapter=new EventListAdapter(getContext(),eventResponse);
        eventListStaggeredView.setAdapter(eventListAdapter);
        eventListAdapter.setClickListener(this);

        sharedPreferences=PreferenceManager .getDefaultSharedPreferences(getActivity());
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        return rootView;
    }

    public EventResponse fetchEventList(String categoryId){

        setViewsVisibility();

        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getContext());
        location=sharedPreferences.getString(getString(R.string.pref_location_key),getString(R.string.default_location));
        sortType=sharedPreferences.getString(getString(R.string.pref_sort_by_key),getString(R.string.default_sort_by));
        searchRadius=Integer.parseInt(sharedPreferences.getString(getString(R.string.pref_radius_key),getString(R.string.default_search_radius)));
        location.replaceAll("\\s","+");

        Log.d("ValuesP",location+" "+sortType+" "+searchRadius);
        ApiInterface apiInterface= ApiClient.getClient().create(ApiInterface.class);

        Call<EventResponse> call=apiInterface.getEventListAccordingToCategory(categoryId,Utility.getDateRangeString(120),location,searchRadius,sortType,PAGE_SIZE,IMAGE_SIZE,BuildConfig.EVENT_API_KEY);
        call.enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                if(response.isSuccessful()){

                    Activity activity=getActivity();
                    if(activity!=null && isAdded()){
                        setEventAttributes(response);
                        if(eventListAdapter!=null) {
                            eventListAdapter.notifyDataSetChanged();
                            spinner.setVisibility(GONE);

                            //checking if no of items fetched are 0.
                            if(response.body().getTotalItems()==0)
                            {
                                eventListStaggeredView.setVisibility(GONE);
                                emptyView.setVisibility(View.VISIBLE);
                                emptyView.setText(R.string.no_results_found);
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<EventResponse> call, Throwable t) {
                Activity activity=getActivity();
                if(activity!=null && isAdded()){
                    spinner.setVisibility(GONE);
                    eventListStaggeredView.setVisibility(GONE);
                    emptyView.setVisibility(View.VISIBLE);
                    emptyView.setText(getString(R.string.error_fetching_data));
                    Log.e(LOG_TAG,"Fetching Event List failed"+t.toString());
                }
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
        Intent intent=new Intent(getActivity(),EventDetailActivity.class);
        intent.putExtra("CallingActivity", ImportantConstants.EVENT_LIST_ACTIVITY);
        intent.putExtra("EventDetailObject",event);
        startActivity(intent);
    }

    private void setViewsVisibility(){
        if(spinner.getVisibility()== GONE || spinner.getVisibility()==View.INVISIBLE)
            spinner.setVisibility(View.VISIBLE);

        if(emptyView.getVisibility()==View.VISIBLE)
            emptyView.setVisibility(GONE);

        if(eventListStaggeredView.getVisibility()==GONE || eventListStaggeredView.getVisibility()==View.INVISIBLE)
            eventListStaggeredView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        eventResponse.getAllEvents().setEventList(new ArrayList<Event>());
        eventListStaggeredView.setVisibility(GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }
}
