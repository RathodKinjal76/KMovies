package com.example.android.kmovies;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Kinjal on 28-06-2018.
 */

public class ModelMovies {

    @SerializedName("original_title")
    private String original_Title;
    @SerializedName("title")
    private String title;
    @SerializedName("poster_path")
    private String poster_Path;
    @SerializedName("overview")
    private String overview;
    @SerializedName("vote_average")
    private Double vote_Average;
    @SerializedName("release_date")
    private String release_Date;
    @SerializedName("backdrop_path")
    private String backdrop_Path;

    public ModelMovies(String original_Title, String title, String poster_Path, String overview, Double vote_Average,
                       String release_Date, String backdrop_Path) {
        this.poster_Path = poster_Path;
        this.title = title;
        this.overview = overview;
        this.release_Date = release_Date;
        this.original_Title = original_Title;
        this.vote_Average = vote_Average;
        this.backdrop_Path = backdrop_Path;
    }

    public String getOriginal_Title() {
        return original_Title;
    }

    public void setOriginal_Title(String original_Title) {
        this.original_Title = original_Title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster_Path() {
        return poster_Path;
    }

    public void setPoster_Path(String poster_Path) {
        this.poster_Path = poster_Path;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Double getVote_Average() {
        return vote_Average;
    }

    public void setVote_Average(Double vote_Average) {
        this.vote_Average = vote_Average;
    }

    public String getRelease_Date() {
        return release_Date;
    }

    public void setRelease_Date(String release_Date) {
        this.release_Date = release_Date;
    }

    public String getBackdrop_Path() {
        return backdrop_Path;
    }

    public void setBackdrop_Path(String backdrop_Path) {
        this.backdrop_Path = backdrop_Path;
    }

}
