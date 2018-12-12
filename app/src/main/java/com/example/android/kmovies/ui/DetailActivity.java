package com.example.android.kmovies.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.kmovies.R;
import com.example.android.kmovies.adapters.ReviewAdapter;
import com.example.android.kmovies.adapters.TrailerAdapter;
import com.example.android.kmovies.data.AppExecutors;
import com.example.android.kmovies.data.MovieDatabase;
import com.example.android.kmovies.models.ModelMovies;
import com.example.android.kmovies.models.ModelReviews;
import com.example.android.kmovies.models.ModelReviewsResponse;
import com.example.android.kmovies.models.ModelTrailers;
import com.example.android.kmovies.models.ModelTrailersResponse;
import com.example.android.kmovies.network.MovieApiClient;
import com.example.android.kmovies.network.MovieApiInterface;
import com.example.android.kmovies.utils.Constants;
import com.example.android.kmovies.utils.Utils;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.android.kmovies.network.MovieApiClient.retrofit;

public class DetailActivity extends AppCompatActivity {
    @BindView(R.id.big_image)
    ImageView bigImage;
    @BindView(R.id.small_image)
    ImageView smallImage;
    @BindView(R.id.tv_title)
    TextView title;
    @BindView(R.id.tv_release_date)
    TextView releaseDate;
    @BindView(R.id.tv_overview)
    TextView overview;
    @BindView(R.id.tv_rating_bar)
    TextView ratingBar;
    @BindView(R.id.rv_reviews)
    RecyclerView recyclerViewReviews;
    @BindView(R.id.rv_trailers)
    RecyclerView recyclerViewTrailers;
    @BindView(R.id.btn_favorite)
    ImageButton favoriteButton;
    @BindView(R.id.detail_toolbar)
    Toolbar toolbar;

    private List<ModelReviews> modelReviews;
    private ReviewAdapter reviewAdapter;
    private List<ModelTrailers> modelTrailers;
    private TrailerAdapter trailerAdapter;

    private FirebaseAnalytics firebaseAnalytics;

    private MovieDatabase movieDatabase;
    private ModelMovies favoriteMovie, tappedMovie;
    private boolean isFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        modelTrailers = new ArrayList<>();
        modelReviews = new ArrayList<>();

        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        movieDatabase = MovieDatabase.getInstance(getApplicationContext());
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        //Setup movie data
        Intent intent = getIntent();
        if (intent != null) {
            tappedMovie = getIntent().getParcelableExtra(Constants.PARC);
            getSupportActionBar().setTitle(tappedMovie.getTitle());
            Picasso.get()
                    .load(Constants.POSTER_URL + tappedMovie.getPosterPath())
                    .placeholder(R.drawable.purple_movie)
                    .error(R.drawable.purple_movie)
                    .into(smallImage);

            Picasso.get()
                    .load(Constants.BACKDROP_URL + tappedMovie.getBackdropPath())
                    .placeholder(R.drawable.purple_movie)
                    .error(R.drawable.purple_movie)
                    .into(bigImage);

            title.setText(tappedMovie.getTitle());
            ratingBar.setText(tappedMovie.getVoteAverage());
            releaseDate.setText(tappedMovie.getReleaseDate());
            overview.setText(tappedMovie.getOverview());

            if (Utils.checkInternet(this)) {
                loadReviews();
                LinearLayoutManager trailerLinearLayoutManager = new LinearLayoutManager(getApplicationContext(),
                        LinearLayoutManager.HORIZONTAL, false);
                recyclerViewReviews.setHasFixedSize(true);
                recyclerViewReviews.setLayoutManager(trailerLinearLayoutManager);
                reviewAdapter = new ReviewAdapter(modelReviews, this);
                recyclerViewReviews.setAdapter(reviewAdapter);

                loadTrailers();
                LinearLayoutManager reviewLinearLayoutManager = new LinearLayoutManager(getApplicationContext(),
                        LinearLayoutManager.HORIZONTAL, false);
                recyclerViewTrailers.setHasFixedSize(true);
                recyclerViewTrailers.setLayoutManager(reviewLinearLayoutManager);
                trailerAdapter = new TrailerAdapter(modelTrailers, this);
                recyclerViewTrailers.setAdapter(trailerAdapter);
            } else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_data), Toast.LENGTH_SHORT).show();
            }
        }


        checkFavorite();
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordFavoriteButtonActivity();
                favoriteTapped();
            }
        });

    }

    void recordFavoriteButtonActivity() {
        String movieId = tappedMovie.getId();
        String movieName = tappedMovie.getTitle();
        Bundle bundle = new Bundle();

        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, movieId);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, movieName);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "movie");
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    private void checkFavorite() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                favoriteMovie = movieDatabase.movieDao().loadMovieById(tappedMovie.getId());
                isFavorite = favoriteMovie != null;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Changing the background of the button.
                        if (isFavorite) {
                            favoriteButton.setBackground(getResources().getDrawable(R.drawable.ic_is_favorite));
                        }
                    }
                });
            }
        });
    }

    private void favoriteTapped() {
        checkFavorite();
        if (isFavorite) {
            //Remove the movie from the favorites database.
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    movieDatabase.movieDao().deleteMovieWithId(tappedMovie.getId());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            favoriteButton.setBackground(getResources().getDrawable(R.drawable.ic_is_favorite_border));
                            isFavorite = false;
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.removed_fav), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

        } else {
            //Add the movie to the favorites database.
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    movieDatabase.movieDao().insert(tappedMovie);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            favoriteButton.setBackground(getResources().getDrawable(R.drawable.ic_is_favorite));
                            isFavorite = true;
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.added_fav), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

        }

    }

    void loadReviews() {

        if (Utils.checkApi(this)) {
            if (Utils.checkInternet(this)) {
                String url = Constants.BASE_URL + tappedMovie.getId() + "/";
                MovieApiInterface apiService =
                        MovieApiClient.getClient(url).create(MovieApiInterface.class);
                Call<ModelReviewsResponse> call = apiService.getReviews(getResources().getString(R.string.api_key));
                call.enqueue(new Callback<ModelReviewsResponse>() {
                    @Override
                    public void onResponse(Call<ModelReviewsResponse> call, Response<ModelReviewsResponse> response) {

                        if (modelReviews != null) {
                            modelReviews.removeAll(modelReviews);
                        }

                        if (response.body().getResults() != null) {
                            modelReviews.addAll(response.body().getResults());
                        } else {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_data), Toast.LENGTH_SHORT).show();
                        }
                        reviewAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onFailure(Call<ModelReviewsResponse> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_data), Toast.LENGTH_SHORT).show();
                    }
                });
                retrofit = null;
            }
        }
    }

    void loadTrailers() {

        if (Utils.checkApi(this)) {
            if (Utils.checkInternet(this)) {
                String url = Constants.BASE_URL + tappedMovie.getId() + "/";
                MovieApiInterface apiService =
                        MovieApiClient.getClient(url).create(MovieApiInterface.class);
                Call<ModelTrailersResponse> call = apiService.getTrailers(getResources().getString(R.string.api_key));
                call.enqueue(new Callback<ModelTrailersResponse>() {
                    @Override
                    public void onResponse(Call<ModelTrailersResponse> call, Response<ModelTrailersResponse> response) {

                        if (modelTrailers != null) {
                            modelTrailers.removeAll(modelTrailers);
                        }

                        if (response.body().getYoutube() != null) {
                            modelTrailers.addAll(response.body().getYoutube());
                        } else {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_data), Toast.LENGTH_SHORT).show();
                        }
                        trailerAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onFailure(Call<ModelTrailersResponse> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_data), Toast.LENGTH_SHORT).show();
                    }
                });
                retrofit = null;
            }
        }
    }

}
