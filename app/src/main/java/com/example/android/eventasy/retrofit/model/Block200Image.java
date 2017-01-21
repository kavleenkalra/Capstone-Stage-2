package com.example.android.eventasy.retrofit.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kakalra on 1/2/2017.
 */

public class Block200Image implements Parcelable{

    @SerializedName("width")
    private String imageWidth;
    @SerializedName("url")
    private String imageUrl;
    @SerializedName("height")
    private String imageHeight;

    public Block200Image(){
    }

    public Block200Image(String imageWidth, String imageUrl, String imageHeight){
        this.imageWidth=imageWidth;
        this.imageUrl=imageUrl;
        this.imageHeight=imageHeight;
    }

    public Block200Image(Parcel in){
        imageWidth=in.readString();
        imageUrl=in.readString();
        imageHeight=in.readString();
    }

    public static final Creator<Block200Image> CREATOR = new Creator<Block200Image>() {
        @Override
        public Block200Image createFromParcel(Parcel in) {
            return new Block200Image(in);
        }

        @Override
        public Block200Image[] newArray(int size) {
            return new Block200Image[size];
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
