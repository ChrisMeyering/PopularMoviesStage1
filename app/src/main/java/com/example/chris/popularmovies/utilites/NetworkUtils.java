package com.example.chris.popularmovies.utilites;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by chris on 9/26/17.
 */

public class NetworkUtils {
    private static final String TMDB_BASE_URL = "https://api.themoviedb.org/3/movie/";
    private static final String TMDB_IMG_BASE_URL = "https://image.tmdb.org/t/p/";
    private static final String PARAM_PAGE_NUMBER = "page";
    private static final String PARAM_API_KEY = "api_key";
    private static final String API_KEY = ""; //Put your own API key here.


    public static URL buildSortedMoviesURL(String sort_by, int pageNumber) {
        Uri builtUri = Uri.parse(TMDB_BASE_URL + sort_by).buildUpon()
                .appendQueryParameter(PARAM_API_KEY,API_KEY)
                .appendQueryParameter(PARAM_PAGE_NUMBER,String.valueOf(pageNumber))
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildMovieURL(int movieID) {
        Uri movieUri = Uri.parse(TMDB_BASE_URL + String.valueOf(movieID)).buildUpon()
                .appendQueryParameter(PARAM_API_KEY,API_KEY)
                .build();
        URL url = null;
        try {
            url = new URL(movieUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String buildBackdropURL(String path, int width) {
        String IMG_SIZE = "w";
        if (width <= 300)
            IMG_SIZE += "300";
        else if (width <= 780)
            IMG_SIZE += "780";
        else if (width <= 1280)
            IMG_SIZE += "1280";
        else
            IMG_SIZE = "original";
        return TMDB_IMG_BASE_URL + IMG_SIZE + path;
    }
    public static String buildPosterURL(String path, int width) {
        String IMG_SIZE = "w";
        if (width <= 92)
            IMG_SIZE += "92";
        else if (width <= 154)
            IMG_SIZE += "154";
        else if (width <= 185)
            IMG_SIZE += "185";
        else if (width <= 342)
            IMG_SIZE += "342";
        else if (width <= 500)
            IMG_SIZE += "500";
        else if (width <= 780)
            IMG_SIZE += "780";
        else
            IMG_SIZE = "original";
        return TMDB_IMG_BASE_URL + IMG_SIZE + path;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            if(scanner.hasNext())
                return scanner.next();
            else
                return null;
        } finally {
            urlConnection.disconnect();
        }
    }
}

