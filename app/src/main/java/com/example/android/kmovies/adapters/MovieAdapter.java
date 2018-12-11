package com.example.android.kmovies.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.kmovies.R;
import com.example.android.kmovies.models.ModelMovies;
import com.example.android.kmovies.ui.DetailActivity;
import com.example.android.kmovies.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<ModelMovies> modelMovies;
    private Context context;
    private Activity activity;

    public MovieAdapter(List<ModelMovies> modelMovies, Context context, Activity activity) {
        this.modelMovies = modelMovies;
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MovieAdapter.MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MovieViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MovieViewHolder holder, final int position) {

        holder.textView.setText(modelMovies.get(position).getTitle());
        Picasso.get()
                .load(Constants.POSTER_URL + modelMovies.get(position).getPosterPath())
                .placeholder(R.drawable.purple_movie)
                .error(R.drawable.purple_movie)
                .into(holder.imageView);

        // Pasing all data to the detail activity through Intent
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra(Constants.PARC, modelMovies.get(position));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity,
                        holder.imageView, ViewCompat.getTransitionName(holder.imageView));
                context.startActivity(intent, options.toBundle());
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelMovies.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.movie_poster)
        ImageView imageView;
        @BindView(R.id.movie_text)
        TextView textView;

        public MovieViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public void update(List<ModelMovies> modelMovies) {
        this.modelMovies = modelMovies;
        notifyDataSetChanged();
    }
}
