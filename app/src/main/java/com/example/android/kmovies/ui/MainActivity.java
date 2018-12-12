package com.example.android.kmovies.ui;

import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.kmovies.R;
import com.example.android.kmovies.adapters.MovieAdapter;
import com.example.android.kmovies.data.MovieViewModel;
import com.example.android.kmovies.models.ModelMovies;
import com.example.android.kmovies.models.ModelMoviesResponse;
import com.example.android.kmovies.network.MovieApiClient;
import com.example.android.kmovies.network.MovieApiInterface;
import com.example.android.kmovies.utils.Constants;
import com.example.android.kmovies.utils.Utils;
import com.example.android.kmovies.widget.MovieWidget;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.android.kmovies.network.MovieApiClient.retrofit;


public class MainActivity extends AppCompatActivity {

    private static String DATA_KEY = "data_key";
    private static String STATE_KEY = "state_key";

    @BindView(R.id.rv_movies)
    RecyclerView recyclerView;

    @BindView(R.id.check_internet)
    TextView textView;

    @BindView(R.id.adView)
    AdView adView;

    @BindView(R.id.main_toolbar)
    Toolbar toolbar;

    private List<ModelMovies> modelMovies;
    private MovieAdapter movieAdapter;
    private Parcelable listState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        MobileAds.initialize(this, getResources().getString(R.string.admob_app_id));
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

        modelMovies = new ArrayList<>();

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        if (savedInstanceState != null) {
            modelMovies = savedInstanceState.getParcelableArrayList(DATA_KEY);
            listState = savedInstanceState.getParcelable(STATE_KEY);
        }

        int spanCount = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ?
                getResources().getInteger(R.integer.num_columns_lanscape) : getResources().getInteger(R.integer.num_columns_portrait);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), spanCount);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);
        movieAdapter = new MovieAdapter(modelMovies, this, MainActivity.this);
        recyclerView.setAdapter(movieAdapter);

        if (modelMovies.size() > 0) {
            textView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            movieAdapter.update(modelMovies);
            if (listState != null) {
                recyclerView.getLayoutManager().onRestoreInstanceState(listState);
            }
        } else {
            loadMovies(Constants.POPULAR);
        }
    }

    void loadMovies(String movieType) {

        if (Utils.checkApi(this, textView, recyclerView)) {
            if (Utils.checkInternet(this, textView, recyclerView)) {
                MovieApiInterface apiService =
                        MovieApiClient.getClient(Constants.BASE_URL).create(MovieApiInterface.class);
                Call<ModelMoviesResponse> call = null;
                if (movieType.equals(Constants.POPULAR))
                    call = apiService.getPopularMovies(getResources().getString(R.string.api_key));
                else if (movieType.equals(Constants.TOP_RATED))
                    call = apiService.getTopRatedMovies(getResources().getString(R.string.api_key));
                call.enqueue(new Callback<ModelMoviesResponse>() {
                    @Override
                    public void onResponse(Call<ModelMoviesResponse> call, Response<ModelMoviesResponse> response) {

                        String[] movieName = new String[5];

                        if (modelMovies != null)
                            modelMovies.removeAll(modelMovies);

                        if (response.body().getResults() != null) {
                            modelMovies.addAll(response.body().getResults());
                        } else {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_data), Toast.LENGTH_SHORT).show();
                        }
                        for (int i = 0; i < 5; i++)
                            movieName[i] = modelMovies.get(i).getTitle();
                        saveDataToSharedPrefs(movieName);
                        movieAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<ModelMoviesResponse> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_in_fetching), Toast.LENGTH_SHORT).show();
                    }
                });
                retrofit = null;
            }
        }
    }


    void favoriteMovies() {
        MovieViewModel viewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        viewModel.getModelMoviesList().observe(this, new Observer<List<ModelMovies>>() {
            @Override
            public void onChanged(@Nullable List<ModelMovies> movies) {
                if (modelMovies.size() > 0) {
                    modelMovies.clear();
                }
                modelMovies.addAll(movies);
                if (modelMovies.size() > 0) {
                    textView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                } else {
                    textView.setText(getResources().getString(R.string.no_fav));
                    textView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
                movieAdapter.notifyDataSetChanged();
            }
        });
    }


    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(DATA_KEY, (ArrayList<ModelMovies>) modelMovies);
        Parcelable listState = recyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(STATE_KEY, listState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            modelMovies = savedInstanceState.getParcelableArrayList(DATA_KEY);
            listState = savedInstanceState.getParcelable(STATE_KEY);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the main; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_filter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.popular_movies) {
            loadMovies(Constants.POPULAR);
            return true;
        }
        if (id == R.id.top_rated_movies) {
            loadMovies(Constants.TOP_RATED);
            return true;
        }
        if (id == R.id.favorites_movies) {
            favoriteMovies();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void saveDataToSharedPrefs(String[] movies) {

        Intent intent = new Intent(getApplicationContext(), MovieWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

        int[] ids = AppWidgetManager.getInstance(getApplicationContext())
                .getAppWidgetIds(new ComponentName(getApplication(), MovieWidget.class));

        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        getApplication().sendBroadcast(intent);
        new MoviesAsyncTask().execute(movies);
    }

    private class MoviesAsyncTask extends AsyncTask<String[], Void, Void> {
        @Override
        protected Void doInBackground(String[]... strings) {
            StringBuilder builder = new StringBuilder();

            for (int i = 0; i < strings.length; i++) {
                if (strings[i] != null) {
                    builder.append(i + 1)
                            .append(". ")
                            .append(strings[i])
                            .append("\n");
                }
            }

            SharedPreferences sharedPref = getApplication().getSharedPreferences(Constants.SHARED_PREF_MOVIE, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(Constants.SHARED_PREF_MOVIE_LIST, builder.toString());
            editor.apply();
            return null;
        }
    }
}
