package com.example.chris.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.chris.popularmovies.utilites.MoviePoster;
import com.example.chris.popularmovies.utilites.NetworkUtils;
import com.example.chris.popularmovies.utilites.Utility;
import com.squareup.picasso.Picasso;

/**
 * Created by chris on 9/27/17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.PosterViewHolder> {
    private MoviePoster[] moviePosters;
    private final Context context;
    private final MovieAdapterClickHandler clickHandler;

    public interface MovieAdapterClickHandler {
        void onClick(int movieID);
    }

    public MovieAdapter(Context context, MovieAdapterClickHandler clickHandler) {
        this.context = context;
        this.clickHandler = clickHandler;
    }

    @Override
    public PosterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.movie_list_item, parent, false);
        return new PosterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PosterViewHolder holder, int position) {
        Picasso.with(context).load(NetworkUtils.buildPosterURL(moviePosters[position].getPoster_path(), Utility.getMaxGridCellWidth(context))).into(holder.iv_poster);
    }

    @Override
    public int getItemCount() {
        if (moviePosters == null) return 0;
        return moviePosters.length;
    }

    public class PosterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final ImageView iv_poster;
        public PosterViewHolder(View view) {
            super(view);
            iv_poster = view.findViewById(R.id.iv_poster);
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            int movieID = moviePosters[getAdapterPosition()].getId();
            clickHandler.onClick(movieID);
        }
    }
    /*
    private final MovieAdapterOnClickHandler clickHandler;

    public interface MovieAdapterOnClickHandler {
        void onClick(MoviePoster poster);
    }


    */
    public void setMoviePosters(MoviePoster[] moviePosters) {
        this.moviePosters = moviePosters;
        notifyDataSetChanged();
    }
}
