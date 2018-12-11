package com.example.android.kmovies.models;

import com.example.android.kmovies.utils.Constants;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelReviewsResponse {
    @SerializedName(Constants.ID)
    private int id;

    @SerializedName(Constants.RESULTS)
    private List<ModelReviews> results;

    @SerializedName(Constants.PAGE)
    private int page;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<ModelReviews> getResults() {
        return results;
    }

    public void setResults(List<ModelReviews> results) {
        this.results = results;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
