package com.example.android.kmovies.models;

import android.graphics.Movie;

import com.example.android.kmovies.utils.Constants;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Kinjal on 28-06-2018.
 */

public class ModelMoviesResponse {
    @SerializedName(Constants.PAGE)
    private int page;

    @SerializedName(Constants.RESULTS)
    private List<ModelMovies> results;

    @SerializedName(Constants.TOTAL_PAGES)
    private int totalPages;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<ModelMovies> getResults() {
        return results;
    }

    public void setResults(List<ModelMovies> results) {
        this.results = results;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
