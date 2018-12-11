package com.example.android.kmovies.models;

import com.example.android.kmovies.utils.Constants;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelTrailersResponse {
    @SerializedName(Constants.ID)
    private int id;

    @SerializedName(Constants.YOUTUBE)
    private List<ModelTrailers> youtube;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<ModelTrailers> getYoutube() {
        return youtube;
    }

    public void setYoutube(List<ModelTrailers> youtube) {
        this.youtube = youtube;
    }
}
