package com.example.android.kmovies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.kmovies.R;
import com.example.android.kmovies.models.ModelReviews;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private List<ModelReviews> modelReviews;
    private Context context;

    public ReviewAdapter(List<ModelReviews> modelReviews, Context context) {
        this.modelReviews = modelReviews;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {

        holder.author.setText(modelReviews.get(position).getAuthor());
        holder.content.setText(modelReviews.get(position).getContent());
    }

    @NonNull
    @Override
    public ReviewAdapter.ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReviewViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.reviews, parent, false));
    }

    @Override
    public int getItemCount() {
        return modelReviews.size();
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.author)
        TextView author;
        @BindView(R.id.content)
        TextView content;

        public ReviewViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
