package com.example.android.kmovies.models;

import com.example.android.kmovies.utils.Constants;
import com.google.gson.annotations.SerializedName;

public class ModelReviews {
    @SerializedName(Constants.REVIEW_AUTHOR)
    private String author;

    @SerializedName(Constants.REVIEW_CONTENT)
    private String content;

    private ModelReviews() {
    }

    public ModelReviews(String author, String content) {
        this.author = author;
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }
}
