package com.example.android.eventasy.retrofit.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kakalra on 12/13/2016.
 */

public class EventImage implements Parcelable{

    @SerializedName("width")
    private int imageWidth;
    @SerializedName("url")
    private String imageUrl;
    @SerializedName("height")
    private int imageHeight;
    @SerializedName("block200")
    private Block200Image block200Image=new Block200Image();
    @SerializedName("large")
    private LargeImage largeImage=new LargeImage();

    public EventImage(){
    }

    public EventImage(int imageWidth, String imageUrl, int imageHeight, Block200Image block200Image, LargeImage largeImage){
        this.imageWidth=imageWidth;
        this.imageUrl=imageUrl;
        this.imageHeight=imageHeight;
        this.block200Image=block200Image;
        this.largeImage=largeImage;
    }

    public EventImage(Parcel in){
        imageWidth=in.readInt();
        imageUrl=in.readString();
        imageHeight=in.readInt();
        block200Image=in.readParcelable(Block200Image.class.getClassLoader());
        largeImage=in.readParcelable(LargeImage.class.getClassLoader());
    }

    public static final Creator<EventImage> CREATOR = new Creator<EventImage>() {
        @Override
        public EventImage createFromParcel(Parcel in) {
            return new EventImage(in);
        }

        @Override
        public EventImage[] newArray(int size) {
            return new EventImage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(imageWidth);
        parcel.writeString(imageUrl);
        parcel.writeInt(imageHeight);
        parcel.writeParcelable(block200Image,PARCELABLE_WRITE_RETURN_VALUE);
        parcel.writeParcelable(largeImage,PARCELABLE_WRITE_RETURN_VALUE);
    }

    public int getImageWidth(){
        return this.imageWidth;
    }

    public void setImageWidth(int imageWidth){
        this.imageWidth=imageWidth;
    }

    public String getImageUrl(){
        return this.imageUrl;
    }

    public void setImageUrl(String imageUrl){
        this.imageUrl=imageUrl;
    }

    public int getImageHeight(){
        return this.imageHeight;
    }

    public void setImageHeight(int imageHeight){
        this.imageHeight=imageHeight;
    }

    public Block200Image getBlock200Image(){
        if(this.block200Image==null)
            this.block200Image=new Block200Image();
        return this.block200Image;
    }

    public void setBlock200Image(Block200Image block200Image){
        this.block200Image=block200Image;
    }

    public LargeImage getLargeImage(){
        if(this.largeImage==null)
            this.largeImage=new LargeImage();
        return this.largeImage;
    }

    public void setLargeImage(LargeImage largeImage){
        this.largeImage=largeImage;
    }
}
