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

public class MovieDetail extends AppCompatActivity {

    private int movieID;
    private TextView tv_display_overview;
    private TextView tv_movie_title;
    private ProgressBar pb_loading_movie;
    private TextView tv_display_error;
    private ImageView iv_backdrop;
    private TextView tv_rating;
    private TextView tv_release_date;
    private ImageView iv_movie_poster;
    private LinearLayout ll_details;
    private LinearLayout ll_synopsys;
    private TextView tv_genres;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        initView();
        Intent start_intent = getIntent();
        if (start_intent != null) {
            if (start_intent.hasExtra("movieID")) {
                movieID = start_intent.getIntExtra("movieID",-1);
            }
        }
        makeMovieQuery();
    }

    private void initView() {
        tv_display_overview = (TextView) findViewById(R.id.tv_movie_info);
        pb_loading_movie = (ProgressBar) findViewById(R.id.pb_loading_movie);
        tv_display_error = (TextView) findViewById(R.id.tv_movie_error);
        iv_backdrop = (ImageView) findViewById(R.id.iv_backdrop);
        tv_movie_title = (TextView) findViewById(R.id.tv_movie_title);
        iv_movie_poster = (ImageView) findViewById(R.id.iv_movie_poster);
        tv_rating = (TextView) findViewById(R.id.tv_rating);
        tv_release_date = (TextView) findViewById(R.id.tv_release_date);
        ll_details = (LinearLayout) findViewById(R.id.details_layout);
        tv_genres = (TextView) findViewById(R.id.tv_genres);
        ll_synopsys = (LinearLayout) findViewById(R.id.synopsys_layout);
    }

    private void makeMovieQuery() {
        URL url = NetworkUtils.buildMovieURL(movieID);
        new MovieQueryTask().execute(url);
    }

    private  void showProgressBar() {
        pb_loading_movie.setVisibility(View.VISIBLE);
        ll_synopsys.setVisibility(View.INVISIBLE);
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
        ll_synopsys.setVisibility(View.VISIBLE);
        tv_display_error.setVisibility(View.INVISIBLE);
        pb_loading_movie.setVisibility(View.INVISIBLE);
    }

    private void showError() {
        pb_loading_movie.setVisibility(View.INVISIBLE);
        tv_genres.setVisibility(View.INVISIBLE);
        iv_backdrop.setVisibility(View.INVISIBLE);
        tv_movie_title.setVisibility(View.INVISIBLE);
        ll_details.setVisibility(View.INVISIBLE);
        ll_synopsys.setVisibility(View.INVISIBLE);
        tv_display_error.setVisibility(View.VISIBLE);
    }

    public class MovieQueryTask extends AsyncTask<URL, Void, MovieInfo> {

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
                tv_genres.setText("Genres: " + movie.getGenre_names());

            } else {
                showError();
            }
        }
    }
}
