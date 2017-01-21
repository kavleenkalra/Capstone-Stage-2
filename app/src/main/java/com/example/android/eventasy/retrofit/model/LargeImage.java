package com.example.android.eventasy.retrofit.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kakalra on 1/5/2017.
 */

public class LargeImage implements Parcelable{

    @SerializedName("width")
    private String imageWidth;
    @SerializedName("url")
    private String imageUrl;
    @SerializedName("height")
    private String imageHeight;

    public LargeImage(){
    }

    public LargeImage(String imageWidth, String imageUrl, String imageHeight){
        this.imageWidth=imageWidth;
        this.imageUrl=imageUrl;
        this.imageHeight=imageHeight;
    }

    public LargeImage(Parcel in){
        imageWidth=in.readString();
        imageUrl=in.readString();
        imageHeight=in.readString();
    }

    public static final Creator<LargeImage> CREATOR = new Creator<LargeImage>() {
        @Override
        public LargeImage createFromParcel(Parcel in) {
            return new LargeImage(in);
        }

        @Override
        public LargeImage[] newArray(int size) {
            return new LargeImage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(imageWidth);
        parcel.writeString(imageUrl);
        parcel.writeString(imageHeight);
    }

    public String getImageWidth(){
        return this.imageWidth;
    }

    public void setImageWidth(String imageWidth){
        this.imageWidth=imageWidth;
    }

    public String getImageUrl(){
        return this.imageUrl;
    }

    public void setImageUrl(String imageUrl){
        this.imageUrl=imageUrl;
    }

    public String getImageHeight(){
        return this.imageHeight;
    }

    public void setImageHeight(String imageHeight){
        this.imageHeight=imageHeight;
    }
}
