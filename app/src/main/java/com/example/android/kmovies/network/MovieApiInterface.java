package com.example.android.kmovies.network;

import com.example.android.kmovies.models.ModelMoviesResponse;
import com.example.android.kmovies.models.ModelReviewsResponse;
import com.example.android.kmovies.models.ModelTrailersResponse;
import com.example.android.kmovies.utils.Constants;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Kinjal on 28-06-2018.
 */

public interface MovieApiInterface {

    @GET(Constants.TOP_RATED)
    Call<ModelMoviesResponse> getTopRatedMovies(@Query(Constants.API_KEY) String apiKey);

    @GET(Constants.POPULAR)
    Call<ModelMoviesResponse> getPopularMovies(@Query(Constants.API_KEY) String apiKey);

    @GET(Constants.REVIEWS)
    Call<ModelReviewsResponse> getReviews(@Query(Constants.API_KEY) String apiKey);

    @GET(Constants.TRAILERS)
    Call<ModelTrailersResponse> getTrailers(@Query(Constants.API_KEY) String apiKey);
}
