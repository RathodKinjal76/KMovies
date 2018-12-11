package com.example.android.kmovies.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.android.kmovies.models.ModelMovies;

import java.util.List;

@Dao
public interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ModelMovies modelMovies);

    @Query("SELECT * FROM movies_table")
    List<ModelMovies> getAllMovies();

    @Query("DELETE FROM movies_table WHERE id= :movie_id")
    void deleteMovieWithId(String movie_id);

    @Query("SELECT * FROM movies_table WHERE id= :movie_id")
    ModelMovies loadMovieById(String movie_id);
}
