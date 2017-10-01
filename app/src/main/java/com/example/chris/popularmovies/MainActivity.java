package com.example.chris.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.chris.popularmovies.utilites.JSONUtils;
import com.example.chris.popularmovies.utilites.MoviePoster;
import com.example.chris.popularmovies.utilites.NetworkUtils;
import com.example.chris.popularmovies.utilites.Utility;

import java.net.URL;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterClickHandler {

    private static final String SORT_BY_RATING = "top_rated";
    private static final String SORT_BY_POPULARITY = "popular";
    private int page_number = 1;
    private String sort_by = SORT_BY_RATING;
    private RecyclerView rv_movies;
    private MovieAdapter movieAdapter;
    private TextView tv_display_error;
    private ProgressBar pb_loading_data;
    private TextView tv_page_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        makeMovieQuery();
    }

    private void initView() {
        rv_movies = (RecyclerView) findViewById(R.id.rv_movies);
        tv_display_error = (TextView) findViewById(R.id.tv_display_error);
        pb_loading_data = (ProgressBar) findViewById(R.id.pb_loading_posters);
        ImageButton ib_prev_page = (ImageButton) findViewById(R.id.ib_prev_page);
        ib_prev_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (page_number > 1) {
                    page_number--;
                    updatePageNumberTV();
                    makeMovieQuery();
                }
            }
        });
        tv_page_num = (TextView) findViewById(R.id.tv_page_num);
        updatePageNumberTV();
        ImageButton ib_next_page = (ImageButton) findViewById(R.id.ib_next_page);
        ib_next_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page_number++;
                updatePageNumberTV();
                makeMovieQuery();
            }
        });
        rv_movies.setHasFixedSize(true);
        rv_movies.setItemViewCacheSize(30);
        rv_movies.setDrawingCacheEnabled(true);
        rv_movies.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, Utility.numOfGridColumns(this));

        rv_movies.setLayoutManager(layoutManager);
        layoutManager.setAutoMeasureEnabled(true);
        movieAdapter = new MovieAdapter(this, this);
        rv_movies.setAdapter(movieAdapter);

    }

    private void updatePageNumberTV() {
        tv_page_num.setText(String.valueOf(page_number));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sort_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort_pop:
                if (!sort_by.equals(SORT_BY_POPULARITY)) {
                    sort_by = SORT_BY_POPULARITY;
                    page_number = 1;
                    updatePageNumberTV();
                    makeMovieQuery();
                }
                return true;
            case R.id.action_sort_rating:
                if (!sort_by.equals(SORT_BY_RATING)) {
                    sort_by = SORT_BY_RATING;
                    page_number = 1;
                    updatePageNumberTV();
                    makeMovieQuery();
                    return true;
                }
            default: return super.onOptionsItemSelected(item);
        }
    }

    private void makeMovieQuery () {
        URL url = NetworkUtils.buildSortedMoviesURL(sort_by, page_number);
        new PosterQueryTask().execute(url);
    }

    private void showMoviePosters() {
        rv_movies.setVisibility(View.VISIBLE);
        tv_display_error.setVisibility(View.INVISIBLE);
    }

    private void showError() {
        rv_movies.setVisibility(View.INVISIBLE);
        tv_display_error.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(int movieID) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra("movieID", movieID);
        startActivity(intent);
    }

    private class PosterQueryTask extends AsyncTask<URL, Void, MoviePoster[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pb_loading_data.setVisibility(View.VISIBLE);
        }

        @Override
        protected MoviePoster[] doInBackground(URL... urls) {
            URL url = urls[0];
            String json_data;
            MoviePoster[] posters = null;
            try {
                json_data = NetworkUtils.getResponseFromHttpUrl(url);
                posters = JSONUtils.getPosterDataFromJson(json_data);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return posters;
        }

        @Override
        protected void onPostExecute(MoviePoster[] posters) {
            pb_loading_data.setVisibility(View.INVISIBLE);
            if (posters != null && posters.length != 0) {
                showMoviePosters();
                movieAdapter.setMoviePosters(posters);
            } else {
                showError();
            }
        }
    }
}
