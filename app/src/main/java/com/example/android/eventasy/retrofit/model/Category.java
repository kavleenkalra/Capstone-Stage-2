package com.example.android.eventasy.retrofit.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kakalra on 12/15/2016.
 */

public class Category implements Parcelable{

    @SerializedName("name")
    private String categoryName;
    @SerializedName("id")
    private String categoryId;

    public Category(String categoryName, String categoryId){
        this.categoryName=categoryName;
        this.categoryId=categoryId;
    }

    public Category(Parcel in){
        categoryName=in.readString();
        categoryId=in.readString();
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(categoryName);
        parcel.writeString(categoryId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getCategoryName(){
            return this.categoryName;
        }

    public void setCategoryName(String categoryName){
            this.categoryName=categoryName;
        }

    public String getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryId(String categoryId){
        this.categoryId=categoryId;
    }

}