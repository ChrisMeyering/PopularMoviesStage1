package com.example.chris.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.chris.popularmovies.utilites.JSONUtils;
import com.example.chris.popularmovies.utilites.MovieInfo;
import com.example.chris.popularmovies.utilites.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity {

    private int movieID;

    @BindView(R.id.tv_movie_info) TextView tv_display_overview;
    @BindView(R.id.tv_movie_title) TextView tv_movie_title;
    @BindView(R.id.pb_loading_movie) ProgressBar pb_loading_movie;
    @BindView(R.id.tv_movie_error) TextView tv_display_error;
    @BindView(R.id.iv_backdrop) ImageView iv_backdrop;
    @BindView(R.id.tv_rating) TextView tv_rating;
    @BindView(R.id.tv_release_date) TextView tv_release_date;
    @BindView(R.id.iv_movie_poster) ImageView iv_movie_poster;
    @BindView(R.id.details_layout) LinearLayout ll_details;
    @BindView(R.id.synopsis_layout) LinearLayout ll_synopsis;
    @BindView(R.id.tv_genres) TextView tv_genres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        ButterKnife.bind(this);
        Intent start_intent = getIntent();
        if (start_intent != null) {
            if (start_intent.hasExtra("movieID")) {
                movieID = start_intent.getIntExtra("movieID",-1);
            }
        }
        makeMovieQuery();
    }

    private void makeMovieQuery() {
        URL url = NetworkUtils.buildMovieURL(movieID);
        new MovieQueryTask().execute(url);
    }

    private  void showProgressBar() {
        pb_loading_movie.setVisibility(View.VISIBLE);
        ll_synopsis.setVisibility(View.INVISIBLE);
        iv_backdrop.setVisibility(View.INVISIBLE);
        tv_movie_title.setVisibility(View.INVISIBLE);
        ll_details.setVisibility(View.INVISIBLE);
        tv_display_error.setVisibility(View.INVISIBLE);
        tv_genres.setVisibility(View.INVISIBLE);
    }

    private void showMovieInfo() {
        tv_movie_title.setVisibility(View.VISIBLE);
        tv_genres.setVisibility(View.VISIBLE);
        iv_backdrop.setVisibility(View.VISIBLE);
        ll_details.setVisibility(View.VISIBLE);
        ll_synopsis.setVisibility(View.VISIBLE);
        tv_display_error.setVisibility(View.INVISIBLE);
        pb_loading_movie.setVisibility(View.INVISIBLE);
    }

    private void showError() {
        pb_loading_movie.setVisibility(View.INVISIBLE);
        tv_genres.setVisibility(View.INVISIBLE);
        iv_backdrop.setVisibility(View.INVISIBLE);
        tv_movie_title.setVisibility(View.INVISIBLE);
        ll_details.setVisibility(View.INVISIBLE);
        ll_synopsis.setVisibility(View.INVISIBLE);
        tv_display_error.setVisibility(View.VISIBLE);
    }

    private class MovieQueryTask extends AsyncTask<URL, Void, MovieInfo> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressBar();
        }

        @Override
        protected MovieInfo doInBackground(URL... urls) {
            URL url = urls[0];
            String json_data;
            MovieInfo movie = null;
            try {
                json_data = NetworkUtils.getResponseFromHttpUrl(url);
                movie = JSONUtils.getMovieDataFromJson(json_data);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return movie;
        }

        @Override
        protected void onPostExecute(MovieInfo movie) {
            if (movie != null) {
                Picasso.with(getBaseContext())
                        .load(NetworkUtils.buildPosterURL(movie.getPoster_path(), 342))
                        .into(iv_movie_poster);
                Picasso.with(getBaseContext()).
                        load(NetworkUtils.buildBackdropURL(movie.getBackdrop_path(), getBaseContext().getResources().getDisplayMetrics().widthPixels))
                        .into(iv_backdrop, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                                showMovieInfo();
                            }
                            @Override
                            public void onError() {
                                showError();
                            }
                        });
                tv_movie_title.setText(movie.getTitle());
                tv_release_date.append(" " + movie.getRelease_date());
                tv_rating.append(" " + String.valueOf(movie.getVote_average()) + "/10");
                tv_display_overview.setText(movie.getOverview());
                tv_genres.append(" " + movie.getGenre_names());

            } else {
                showError();
            }
        }
    }
}
