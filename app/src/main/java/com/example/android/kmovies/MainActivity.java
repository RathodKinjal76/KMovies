package com.example.android.kmovies;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    // TODO - insert your themoviedb.org API KEY here
    private final static String API_KEY = "2c33977cd66a8f48ed063fe005a3ffd3";

    GridView movie_list_grid;
    List<ModelMovies> movies;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (API_KEY.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please obtain your API KEY first from themoviedb.org", Toast.LENGTH_LONG).show();
            return;
        }
        movie_list_grid = (GridView) findViewById(R.id.grid_movie_list);
        popular_movies();

    }

    void popular_movies() {
        MovieApiInterface apiService =
                MovieApiClient.getClient().create(MovieApiInterface.class);
        Call<ModelMoviesResponse> call = apiService.getPopularMovies(API_KEY);

        call.enqueue(new Callback<ModelMoviesResponse>() {
            @Override
            public void onResponse(Call<ModelMoviesResponse> call, Response<ModelMoviesResponse> response) {
                movies = response.body().getResults();
                myadapter obj = new myadapter(MainActivity.this);
                movie_list_grid.setAdapter(obj);
                Log.d(TAG, "Number of movies received: " + movies.size());
            }

            @Override
            public void onFailure(Call<ModelMoviesResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });
    }

    void top_rated_movies() {
        MovieApiInterface apiService =
                MovieApiClient.getClient().create(MovieApiInterface.class);
        Call<ModelMoviesResponse> call = apiService.getTopRatedMovies(API_KEY);

        call.enqueue(new Callback<ModelMoviesResponse>() {
            @Override
            public void onResponse(Call<ModelMoviesResponse> call, Response<ModelMoviesResponse> response) {
                movies = response.body().getResults();
                myadapter obj = new myadapter(MainActivity.this);
                movie_list_grid.setAdapter(obj);
                Log.d(TAG, "Number of movies received: " + movies.size());
            }

            @Override
            public void onFailure(Call<ModelMoviesResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });
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
            Toast.makeText(getApplicationContext(), item.getTitle() + "", Toast.LENGTH_SHORT).show();
            popular_movies();
            return true;
        }
        if (id == R.id.top_rated_movies) {
            Toast.makeText(getApplicationContext(), item.getTitle() + "", Toast.LENGTH_SHORT).show();
            top_rated_movies();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class myadapter extends BaseAdapter {

        Context contxt;
        LayoutInflater inflater;

        class ViewHolder {
            ImageView img;
            TextView txt;
        }

        public myadapter(Context mycontext) {
            contxt = mycontext;
            inflater = (LayoutInflater) contxt
                    .getSystemService(contxt.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return movies.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(final int arg0, View arg1, ViewGroup arg2) {
            // TODO Auto-generated method stub
            ViewHolder Holder;
            if (arg1 == null) {
                arg1 = inflater.inflate(R.layout.card_row, null);
                Holder = new ViewHolder();
                Holder.img = (ImageView) arg1.findViewById(R.id.movie_poster);
                Holder.txt = (TextView) arg1.findViewById(R.id.movie_text);
                arg1.setTag(Holder);
            } else {
                Holder = (ViewHolder) arg1.getTag();
            }

            //final ModelCategory modelCategory = arr.get(arg0);
            Picasso.with(MainActivity.this).load("http://image.tmdb.org/t/p/w185//" + movies.get(arg0).getPoster_Path()).placeholder(R.drawable.pop_movies)
                    .into(Holder.img);
            Holder.txt.setText(movies.get(arg0).getTitle());
            arg1.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                    intent.putExtra("Title", movies.get(arg0).getTitle());
                    intent.putExtra("OriginalTitle", movies.get(arg0).getOriginal_Title());
                    intent.putExtra("BackdropPath", movies.get(arg0).getBackdrop_Path());
                    intent.putExtra("PosterPath", movies.get(arg0).getPoster_Path());
                    intent.putExtra("Rating", movies.get(arg0).getVote_Average());
                    intent.putExtra("ReleaseDate", movies.get(arg0).getRelease_Date());
                    intent.putExtra("Overview", movies.get(arg0).getOverview());
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "You Clicked " + movies.get(arg0).getOriginal_Title(), Toast.LENGTH_LONG).show();
                }
            });
            return arg1;

        }

    }
}
