package com.example.android.eventasy.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.example.android.eventasy.adapters.CategoryAdapter;
import com.example.android.eventasy.retrofit.model.Category;
import com.example.android.eventasy.retrofit.model.CategoryResponse;
import com.example.android.eventasy.retrofit.rest.ApiClient;
import com.example.android.eventasy.retrofit.rest.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment implements ItemClickListener {

    private final String LOG_TAG=CategoryFragment.class.getSimpleName();

    RecyclerView categoryGridView;
    TextView emptyView;
    ProgressBar spinner;
    GridLayoutManager gridLayoutManager;
    CategoryAdapter adapter;
    CategoryResponse categoryResponse;

    public CategoryFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState==null || !savedInstanceState.containsKey("Categories")){
            categoryResponse=new CategoryResponse();
        }
        else
        {
            categoryResponse=savedInstanceState.getParcelable("Categories");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if(categoryResponse.getCategoryList().size()==0)
        {
            if(Utility.isNetworkAvailable(getContext())){
                categoryResponse=fetchCategories();
            }
            else{
                categoryGridView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
                emptyView.setText(getString(R.string.no_network_available));
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("Categories",categoryResponse);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

        View rootView=inflater.inflate(R.layout.fragment_category, container, false);

        categoryGridView=(RecyclerView)rootView.findViewById(R.id.categories_recycler_view);
        categoryGridView.setHasFixedSize(true);
        int noOfColumns=getActivity().getResources().getConfiguration().orientation== Configuration.ORIENTATION_PORTRAIT?ImportantConstants.PORTRAIT_COLUMNS:ImportantConstants.LANDSCAPE_COLUMNS;
        gridLayoutManager=new GridLayoutManager(getActivity(),noOfColumns,GridLayoutManager.VERTICAL,false);
        categoryGridView.setLayoutManager(gridLayoutManager);

        emptyView=(TextView)rootView.findViewById(R.id.empty_category_view);
        spinner=(ProgressBar)rootView.findViewById(R.id.category_progress_bar);

        adapter=new CategoryAdapter(getContext(),categoryResponse);
        categoryGridView.setAdapter(adapter);
        adapter.setClickListener(this);

        return rootView;
    }

    public CategoryResponse fetchCategories() {
        setViewsVisibility();
        ApiInterface apiInterface= ApiClient.getClient().create(ApiInterface.class);

        Call<CategoryResponse> call=apiInterface.getCategories(BuildConfig.EVENT_API_KEY);
        call.enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                if (response.isSuccessful()){
                    Activity activity=getActivity();
                    if(isAdded() && activity!=null){
                        categoryResponse.setCategoryList(response.body().getCategoryList());
                        if (adapter!=null){
                            adapter.notifyDataSetChanged();
                            spinner.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {
                Activity activity=getActivity();
                if(isAdded() && activity!=null) {
                    spinner.setVisibility(View.GONE);
                    categoryGridView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                    emptyView.setText(getString(R.string.error_fetching_data));
                    Log.e(LOG_TAG,"Fetching categories failed "+t.toString());
                }
            }
        });
        return categoryResponse;
    }

    @Override
    public void onClick(View view, int position) {
        Category c=categoryResponse.getCategoryList().get(position);
        Intent intent=new Intent(getActivity(),EventListActivity.class);
        intent.putExtra("CategoryObject",c);
        startActivity(intent);
    }

    private void setViewsVisibility(){
        if(spinner.getVisibility()== GONE || spinner.getVisibility()==View.INVISIBLE)
            spinner.setVisibility(View.VISIBLE);

        if(emptyView.getVisibility()==View.VISIBLE)
            emptyView.setVisibility(GONE);

        if(categoryGridView.getVisibility()==GONE || categoryGridView.getVisibility()==View.INVISIBLE)
            categoryGridView.setVisibility(View.VISIBLE);
    }
}