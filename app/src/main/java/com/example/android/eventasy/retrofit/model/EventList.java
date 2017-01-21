package com.example.android.eventasy.retrofit.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kakalra on 12/18/2016.
 */

public class EventList implements Parcelable{

    @SerializedName("event")
    private List<Event> eventList=new ArrayList<Event>();

    public EventList(){
    }

    public EventList(Parcel in){
        in.readTypedList(eventList,Event.CREATOR);
    }

    public static final Creator<EventList> CREATOR = new Creator<EventList>() {
        @Override
        public EventList createFromParcel(Parcel in) {
            return new EventList(in);
        }

        @Override
        public EventList[] newArray(int size) {
            return new EventList[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(eventList);
    }

    public List<Event> getEventList(){
        if(this.eventList==null)
            this.eventList=new ArrayList<Event>();
        return this.eventList;
    }

    public void setEventList(List<Event> eventList){
        this.eventList=eventList;
    }
}
