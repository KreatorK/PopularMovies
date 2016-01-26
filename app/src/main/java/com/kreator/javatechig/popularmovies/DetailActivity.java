package com.kreator.javatechig.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    TextView detailTitleTV;
    TextView synopsisTV;
    TextView releaseDateTV;
    TextView userRatingTV;

    ImageView posterIV;
    ImageView backdropIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        detailTitleTV = (TextView) findViewById(R.id.detail_title);
        synopsisTV = (TextView) findViewById(R.id.detail_synopsis);
        releaseDateTV = (TextView) findViewById(R.id.detail_release_date);
        userRatingTV = (TextView) findViewById(R.id.detail_user_rating);
        posterIV = (ImageView) findViewById(R.id.detail_thumbnail);
        backdropIV = (ImageView) findViewById(R.id.detail_backdrop);

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            String title = bundle.getString("original_title");

            String posterPath = bundle.getString("poster_path");
            String backdropPath = bundle.getString("backdrop_path");
            String releaseDate = bundle.getString("release_date");
            String synopsis = bundle.getString("synopsis");
            String userRating = bundle.getString("user_rating");


            detailTitleTV.setText(title);
            synopsisTV.setText(synopsis);
            releaseDateTV.setText(releaseDate);
            userRatingTV.setText(userRating);

            //Download image using picasso library
            String link = "http://image.tmdb.org/t/p/w185/" + posterPath;
            Picasso.with(getApplicationContext()).load(link)
                    .error(R.mipmap.ic_launcher)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(posterIV);


            link = "http://image.tmdb.org/t/p/w185/" + backdropPath;
            /*Picasso.with(getApplicationContext()).load(link)
                    .error(R.mipmap.ic_launcher)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(backdropIV);*/


        }

    }

}
