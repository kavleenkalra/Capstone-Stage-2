package com.example.android.eventasy.retrofit.rest;

import com.example.android.eventasy.retrofit.model.Category;
import com.example.android.eventasy.retrofit.model.CategoryResponse;
import com.example.android.eventasy.retrofit.model.Event;
import com.example.android.eventasy.retrofit.model.EventResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by kakalra on 12/14/2016.
 */

public interface ApiInterface {

    @GET("events/get")
    Call<Event> getEventDetail(@Query("id") String id,
                               @Query("app_key") String apiKey);

    @GET("events/search")
    Call<EventResponse> getUpcomingEventList(@Query("date") String date,
                                             @Query("sort_order") String sortOrder,
                                             @Query("sort_direction") String sort_direction,
                                             @Query("page_size") int pageSize,
                                             @Query("image_sizes") String imageSize,
                                             @Query("app_key") String apiKey);

    @GET("categories/list")
    Call<CategoryResponse> getCategories(@Query("app_key") String apiKey);

    @GET("events/search")
    Call<EventResponse> getEventListAccordingToCategory(@Query("category") String category,
                                                        @Query("date") String date,
                                                        @Query("location") String location,
                                                        @Query("within") int radius,
                                                        @Query("sort_order") String sortType,
                                                        @Query("page_size") int pageSize,
                                                        @Query("image_sizes") String imageSize,
                                                        @Query("app_key") String apiKey);

    @GET("events/search")
    Call<EventResponse> getSearchResults(@Query("q") String query,
                                         @Query("date") String date,
                                         @Query("page_size") int pageSize,
                                         @Query("image_sizes") String imageSize,
                                         @Query("app_key") String apiKey);

}
