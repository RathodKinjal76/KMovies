package com.example.android.kmovies.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.android.kmovies.models.ModelMovies;
import com.example.android.kmovies.utils.Constants;

@Database(entities = {ModelMovies.class}, version = 1, exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {

    private static final Object LOCK = new Object();
    private static MovieDatabase sInstance;

    public static MovieDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                //Creating a new database instance.
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        MovieDatabase.class, Constants.DATABASE)
                        .build();
            }
        }
        //Getting the database instance.
        return sInstance;
    }

    public abstract MovieDao movieDao();
}