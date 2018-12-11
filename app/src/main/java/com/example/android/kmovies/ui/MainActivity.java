package com.example.android.kmovies.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.kmovies.R;
import com.example.android.kmovies.adapters.MovieAdapter;
import com.example.android.kmovies.data.AppExecutors;
import com.example.android.kmovies.data.MovieDatabase;
import com.example.android.kmovies.models.ModelMovies;
import com.example.android.kmovies.models.ModelMoviesResponse;
import com.example.android.kmovies.network.MovieApiClient;
import com.example.android.kmovies.network.MovieApiInterface;
import com.example.android.kmovies.utils.Constants;
import com.example.android.kmovies.utils.Utils;

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

    private ModelMoviesResponse modelMoviesResponse;
    private List<ModelMovies> modelMovies;
    private List<ModelMovies> favoriteMovies;
    private MovieAdapter movieAdapter;
    private Parcelable listState;
    private MovieDatabase movieDatabase;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        modelMovies = new ArrayList<>();
        favoriteMovies = new ArrayList<>();
        movieDatabase = MovieDatabase.getInstance(getApplicationContext());

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

                        if (modelMovies != null)
                            modelMovies.removeAll(modelMovies);

                        if (response.body().getResults() != null) {
                            modelMovies.addAll(response.body().getResults());
                        } else {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_data), Toast.LENGTH_SHORT).show();
                        }
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
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                favoriteMovies = movieDatabase.movieDao().getAllMovies();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (modelMovies.size() > 0) {
                            modelMovies.clear();
                        }
                        modelMovies.addAll(favoriteMovies);
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
}
