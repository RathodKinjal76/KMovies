package com.example.android.kmovies;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Kinjal on 28-06-2018.
 */

public class ModelMoviesResponse {
    @SerializedName("page")
    private int page;
    @SerializedName("results")
    private List<ModelMovies> results;
    @SerializedName("total_results")
    private int totalResults;
    @SerializedName("total_pages")
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

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
