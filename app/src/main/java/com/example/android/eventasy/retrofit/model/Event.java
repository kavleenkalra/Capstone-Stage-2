package com.example.android.eventasy.retrofit.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kakalra on 12/13/2016.
 */

public class Event implements Parcelable{

    @SerializedName("id")
    private String eventId;
    @SerializedName("url")
    private String eventUrl;
    @SerializedName("title")
    private String eventTitle;
    @SerializedName("description")
    private String eventDescription;
    @SerializedName("start_time")
    private String eventStartTime;
    @SerializedName("stop_time")
    private String eventStopTime;
    @SerializedName("all_day")
    private boolean isAllDayEvent;
    @SerializedName("venue_name")
    private String eventVenueName;
    @SerializedName("venue_id")
    private String eventVenueId;
    @SerializedName("address")
    private String eventAddress;
    @SerializedName("city")
    private String eventCity;
    @SerializedName("region")
    private String eventRegion;
    @SerializedName("postal_code")
    private String eventPostalCode;
    @SerializedName("country")
    private String eventCountry;
    @SerializedName("latitude")
    private float eventLatitude;
    @SerializedName("longitude")
    private float eventLongitude;
    @SerializedName("privacy")
    private int isEventPrivate;
    @SerializedName("free")
    private boolean isEventFree;
    @SerializedName("price")
    private String eventPrice;
    @SerializedName("image")
    private EventImage eventImage=new EventImage();

    public Event(){
    }

    public Event(String eventId, String eventUrl, String eventTitle, String eventDescription, String eventStartTime, String eventStopTime, boolean isAllDayEvent, String eventVenueName, String eventVenueId, String eventAddress, String eventCity, String eventRegion, String eventPostalCode, String eventCountry, float eventLatitude, float eventLongitude, int isEventPrivate, boolean isEventFree, String eventPrice, EventImage eventImage)
    {
        this.eventId=eventId;
        this.eventUrl=eventUrl;
        this.eventTitle=eventTitle;
        this.eventDescription=eventDescription;
        this.eventStartTime=eventStartTime;
        this.eventStopTime=eventStopTime;
        this.isAllDayEvent=isAllDayEvent;
        this.eventVenueName=eventVenueName;
        this.eventVenueId=eventVenueId;
        this.eventAddress=eventAddress;
        this.eventCity=eventCity;
        this.eventRegion=eventRegion;
        this.eventPostalCode=eventPostalCode;
        this.eventCountry=eventCountry;
        this.eventLatitude=eventLatitude;
        this.eventLongitude=eventLongitude;
        this.isEventPrivate=isEventPrivate;
        this.isEventFree=isEventFree;
        this.eventPrice=eventPrice;
        this.eventImage=eventImage;
    }

    public Event(Parcel in){
        eventId=in.readString();
        eventUrl=in.readString();
        eventTitle=in.readString();
        eventDescription=in.readString();
        eventStartTime=in.readString();
        eventStopTime=in.readString();
        isAllDayEvent=in.readByte()!=0;
        eventVenueName=in.readString();
        eventVenueId=in.readString();
        eventAddress=in.readString();
        eventCity=in.readString();
        eventRegion=in.readString();
        eventPostalCode=in.readString();
        eventCountry=in.readString();
        eventLatitude=in.readFloat();
        eventLongitude=in.readFloat();
        isEventPrivate=in.readInt();
        isEventFree=in.readByte()!=0;
        eventPrice=in.readString();
        eventImage=in.readParcelable(getClass().getClassLoader());
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(eventId);
        parcel.writeString(eventUrl);
        parcel.writeString(eventTitle);
        parcel.writeString(eventDescription);
        parcel.writeString(eventStartTime);
        parcel.writeString(eventStopTime);
        parcel.writeByte((byte)(isAllDayEvent?1:0));
        parcel.writeString(eventVenueName);
        parcel.writeString(eventVenueId);
        parcel.writeString(eventAddress);
        parcel.writeString(eventCity);
        parcel.writeString(eventRegion);
        parcel.writeString(eventPostalCode);
        parcel.writeString(eventCountry);
        parcel.writeFloat(eventLatitude);
        parcel.writeFloat(eventLongitude);
        parcel.writeInt(isEventPrivate);
        parcel.writeByte((byte)(isEventFree?1:0));
        parcel.writeString(eventPrice);
        parcel.writeParcelable(eventImage,PARCELABLE_WRITE_RETURN_VALUE);
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId=eventId;
    }

    public String getEventUrl()
    {
        return eventUrl;
    }

    public void setEventUrl(String eventUrl){
        this.eventUrl=eventUrl;
    }

    public String getEventTitle(){
        return this.eventTitle;
    }

    public void setEventTitle(String eventTitle){
        this.eventTitle=eventTitle;
    }

    public String getEventDescription(){
        return this.eventDescription;
    }

    public void setEventDescription(String eventDescription){
        this.eventDescription=eventDescription;
    }

    public String getEventStartTime(){
        return this.eventStartTime;
    }

    public void setEventStartTime(String eventStartTime){
        this.eventStartTime=eventStartTime;
    }

    public String getEventStopTime(){
        return this.eventStopTime;
    }

    public void setEventStopTime(String eventStopTime){
        this.eventStopTime=eventStopTime;
    }

    public boolean getIsAllDayEvent (){
        return this.isAllDayEvent;
    }

    public void setIsAllDayEvent(boolean isAllDayEvent){
        this.isAllDayEvent=isAllDayEvent;
    }

    public String getEventVenueName(){
        return this.eventVenueName;
    }

    public void setEventVenueName(String eventVenueName){
        this.eventVenueName=eventVenueName;
    }

    public String getEventVenueId(){
        return this.eventVenueId;
    }

    public String getEventAddress(){
        return this.eventAddress;
    }

    public void setEventAddress(String eventAddress){
        this.eventAddress=eventAddress;
    }

    public String getEventCity(){
        return this.eventCity;
    }

    public void setEventCity(String eventCity){
        this.eventCity=eventCity;
    }

    public String getEventRegion(){
        return this.eventRegion;
    }

    public void setEventRegion(String eventRegion){
        this.eventRegion=eventRegion;
    }

    public String getEventPostalCode(){
        return this.eventPostalCode;
    }

    public void setEventPostalCode(String eventPostalCode){
        this.eventPostalCode=eventPostalCode;
    }

    public String getEventCountry(){
        return this.eventCountry;
    }

    public void setEventCountry(String eventCountry){
        this.eventCountry=eventCountry;
    }

    public Float getEventLatitude(){
        return this.eventLatitude;
    }

    public void setEventLatitude(Float eventLatitude){
        this.eventLatitude=eventLatitude;
    }

    public Float getEventLongitude(){
        return this.eventLongitude;
    }

    public void setEventLongitude(Float eventLongitude){
        this.eventLongitude=eventLongitude;
    }

    public int getIsEventPrivate(){
        return this.isEventPrivate;
    }

    public void setIsEventPrivate(int isEventPrivate){
        this.isEventPrivate=isEventPrivate;
    }

    public boolean getIsEventFree(){
        return this.isEventFree;
    }

    public void setIsEventFree(boolean isEventFree){
        this.isEventFree=isEventFree;
    }

    public String getEventPrice(){
        return this.eventPrice;
    }

    public void setEventPrice(String eventPrice){
        this.eventPrice=eventPrice;
    }

    public EventImage getEventImage(){
        if(this.eventImage==null)
            this.eventImage=new EventImage();
        return this.eventImage;
    }

    public void setEventImage(EventImage eventImage){
        this.eventImage=eventImage;
    }
}
