package com.example.chris.popularmovies.utilites;

/**
 * Created by chris on 9/27/17.
 */

public class MoviePoster {
    private final int id;
    private final String poster_path;

    MoviePoster(int id, String poster_path) {
        this.id = id;
        this.poster_path = poster_path;
    }

    public int getId() {
        return id;
    }

    public String getPoster_path() {
        return poster_path;
    }
}
