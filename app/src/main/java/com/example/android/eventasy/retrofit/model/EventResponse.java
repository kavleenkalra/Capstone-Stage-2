package com.example.android.eventasy.retrofit.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kakalra on 12/18/2016.
 */

public class EventResponse implements Parcelable{

    @SerializedName("page_number")
    private int pageNumber;
    @SerializedName("page_size")
    private int pageSize;
    @SerializedName("total_items")
    private int totalItems;
    @SerializedName("events")
    private EventList allEvents=new EventList();

    public EventResponse(){
    }

    public EventResponse(int pageNumber,int pageSize, int totalItems, EventList allEvents){
        this.pageNumber=pageNumber;
        this.pageSize=pageSize;
        this.totalItems=totalItems;
        this.allEvents=allEvents;
    }

    public EventResponse(Parcel in){
        pageNumber=in.readInt();
        pageSize=in.readInt();
        totalItems=in.readInt();
        allEvents=in.readParcelable(getClass().getClassLoader());
    }

    public static final Creator<EventResponse> CREATOR = new Creator<EventResponse>() {
        @Override
        public EventResponse createFromParcel(Parcel in) {
            return new EventResponse(in);
        }

        @Override
        public EventResponse[] newArray(int size) {
            return new EventResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(pageNumber);
        parcel.writeInt(pageSize);
        parcel.writeInt(totalItems);
        parcel.writeParcelable(allEvents,PARCELABLE_WRITE_RETURN_VALUE);
    }

    public int getPageNumber(){
        return this.pageNumber;
    }

    public void setPageNumber(int pageNumber){
        this.pageNumber=pageNumber;
    }

    public int getPageSize(){
        return this.pageSize;
    }

    public void setPageSize(int pageSize){
        this.pageSize=pageSize;
    }

    public int getTotalItems(){
        return this.totalItems;
    }

    public void setTotalItems(int totalItems){
        this.totalItems=totalItems;
    }

    public EventList getAllEvents(){
        if(this.allEvents==null)
            this.allEvents=new EventList();
        return this.allEvents;
    }

    public void setAllEvents(EventList allEvents){
        this.allEvents=allEvents;
    }
}
