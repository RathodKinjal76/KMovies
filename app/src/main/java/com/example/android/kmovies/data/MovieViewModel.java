package com.example.android.kmovies.data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.android.kmovies.models.ModelMovies;

import java.util.List;

public class MovieViewModel extends AndroidViewModel {


    private LiveData<List<ModelMovies>> modelMovieList;

    public LiveData<List<ModelMovies>> getModelMoviesList() {

        return modelMovieList;
    }

    public MovieViewModel(@NonNull Application application) {
        super(application);
        MovieDatabase database = MovieDatabase.getInstance(this.getApplication());
        modelMovieList = database.movieDao().getAllMovies();
    }
}
