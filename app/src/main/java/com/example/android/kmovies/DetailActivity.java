package com.example.android.kmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {
    @BindView(R.id.big_image)
    ImageView big_Image;
    @BindView(R.id.small_image)
    ImageView small_Image;
    @BindView(R.id.tv_title)
    TextView title;
    @BindView(R.id.tv_relese_date)
    TextView release_Date;
    @BindView(R.id.tv_overview)
    TextView overview;
    @BindView(R.id.tv_rating_bar)
    TextView rating_Bar;

    String OriginalTitle, Title, BackdropPath, PosterPath, ReleaseDate, Overview;
    Double Rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Title = getIntent().getExtras().getString("Title");
        getSupportActionBar().setTitle(Title);
        OriginalTitle = getIntent().getExtras().getString("OriginalTitle");
        BackdropPath = getIntent().getExtras().getString("BackdropPath");
        PosterPath = getIntent().getExtras().getString("PosterPath");
        Rating = getIntent().getExtras().getDouble("Rating");
        ReleaseDate = getIntent().getExtras().getString("ReleaseDate");
        Overview = getIntent().getExtras().getString("Overview");

        Picasso.with(DetailActivity.this).load("http://image.tmdb.org/t/p/w185//" + BackdropPath).placeholder(R.drawable.pop_movies)
                .into(big_Image);

        Picasso.with(DetailActivity.this).load("http://image.tmdb.org/t/p/w185//" + PosterPath).placeholder(R.drawable.pop_movies)
                .into(small_Image);

        title.setText(OriginalTitle);
        release_Date.setText(ReleaseDate);
        overview.setText(Overview);
        rating_Bar.setText((String.valueOf(Rating)));

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(DetailActivity.this, MainActivity.class);
        startActivity(intent);
        super.onBackPressed();

    }
}
