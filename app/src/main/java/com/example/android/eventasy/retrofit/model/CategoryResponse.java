package com.example.android.eventasy.retrofit.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kakalra on 12/18/2016.
 */

public class CategoryResponse implements Parcelable{

    @SerializedName("category")
    List<Category> categories=new ArrayList<Category>();

    public CategoryResponse(){
    }

    public CategoryResponse(Parcel in){
        in.readTypedList(categories,Category.CREATOR);
    }

    public static final Creator<CategoryResponse> CREATOR = new Creator<CategoryResponse>() {
        @Override
        public CategoryResponse createFromParcel(Parcel in) {
            return new CategoryResponse(in);
        }

        @Override
        public CategoryResponse[] newArray(int size) {
            return new CategoryResponse[size];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(categories);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public List<Category> getCategoryList(){
        if(this.categories==null)
            this.categories=new ArrayList<Category>();
        return this.categories;
    }

    public void setCategoryList(List<Category> categories){
        this.categories=categories;
    }
}
