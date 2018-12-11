package com.example.android.kmovies.models;

import com.example.android.kmovies.utils.Constants;
import com.google.gson.annotations.SerializedName;

public class ModelTrailers {
    @SerializedName(Constants.TRAILER_NAME)
    private String name;

    @SerializedName(Constants.TRAILER_SOURCE)
    private String source;

    private ModelTrailers() {

    }

    private ModelTrailers(String name, String source) {
        this.name = name;
        this.source = source;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return source;
    }
}
