package com.example.android.kmovies;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Kinjal on 28-06-2018.
 */

public interface MovieApiInterface {

    @GET("movie/top_rated")
    Call<ModelMoviesResponse> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("movie/popular")
    Call<ModelMoviesResponse> getPopularMovies(@Query("api_key") String apiKey);


  /*  @GET("movie/{id}")
    Call<ModelMovies> getMovieDetails(@Path("id") int id, @Query("api_key") String apiKey);*/
}
