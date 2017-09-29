package com.example.chris.popularmovies.utilites;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by chris on 9/26/17.
 */




public class JSONUtils {
    private static final String RESULTS = "results";
    private static final String MOV_VOTE_COUNT = "vote_count";
    private static final String MOV_ID = "id";
    private static final String MOV_VIDEO = "video";
    private static final String MOV_VOTE_AVG = "vote_average";
    private static final String MOV_TITLE = "title";
    private static final String MOV_POPULARITY = "popularity";
    private static final String MOV_POSTER = "poster_path";
    private static final String MOV_ORIG_LANG = "original_language";
    private static final String MOV_ORIG_TITLE = "original_title";
    private static final String MOV_GENRES = "genres";
    private static final String MOV_BACKDROP_PATH = "backdrop_path";
    private static final String MOV_ADULT = "adult";
    private static final String MOV_OVERVIEW = "overview";
    private static final String MOV_REL_DATE = "release_date";

    public static MovieInfo getMovieDataFromJson(String MovieJSONString) throws JSONException {
        MovieInfo movie = new MovieInfo();
        JSONObject movieData = new JSONObject(MovieJSONString);
        movie.setVote_count(movieData.getInt(MOV_VOTE_COUNT));
        movie.setId(movieData.getInt(MOV_ID));
        movie.setVideo(movieData.getBoolean(MOV_VIDEO));
        movie.setVote_average(movieData.getDouble(MOV_VOTE_AVG));
        movie.setTitle(movieData.getString(MOV_TITLE));
        movie.setPopularity(movieData.getLong(MOV_POPULARITY));
        movie.setPoster_path(movieData.getString(MOV_POSTER));
        movie.setOriginal_language(movieData.getString(MOV_ORIG_LANG));
        movie.setOriginal_title(movieData.getString(MOV_ORIG_TITLE));
        JSONArray JSON_Genres = movieData.getJSONArray(MOV_GENRES);
        if (JSON_Genres != null) {
            String[] genre_names = new String[JSON_Genres.length()];
            for (int j = 0; j < JSON_Genres.length(); j++) {
                JSONObject genres = JSON_Genres.getJSONObject(j);
                genre_names[j] = genres.getString("name");
            }
            movie.setGenre_names(genre_names);
        }
        movie.setBackdrop_path(movieData.getString(MOV_BACKDROP_PATH));
        movie.setAdult(movieData.getBoolean(MOV_ADULT));
        movie.setOverview(movieData.getString(MOV_OVERVIEW));
        movie.setRelease_date(movieData.getString(MOV_REL_DATE));
        return movie;
    }

    public static MoviePoster[] getPosterDataFromJson(String JSONString) throws JSONException {
        MoviePoster[] posters;
        JSONObject queryResults = new JSONObject(JSONString);
        JSONArray JSONMovies = queryResults.getJSONArray(RESULTS);
        posters = new MoviePoster[JSONMovies.length()];
        for (int i =0; i< JSONMovies.length(); i++) {
            JSONObject JSONMovie = JSONMovies.getJSONObject(i);
            posters[i] = new MoviePoster(JSONMovie.getInt(MOV_ID), JSONMovie.getString(MOV_POSTER));
        }
        return posters;
    }
}
