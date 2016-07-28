package com.example.denzale.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Denzale on 3/2/2016.
 */
public class PopularMovieRecyclerViewAdapter extends RecyclerView.Adapter<PopularMovieRecyclerViewAdapter.ViewHolder> {

    private List<PopularMovie> moviePosters;
    private FragmentActivity mContext;
    private Boolean inFavorites;

    public PopularMovieRecyclerViewAdapter(Context context, List<PopularMovie> moviePosters, Boolean inFavorites) {
        mContext = (FragmentActivity) context;
        this.moviePosters = moviePosters;
        this.inFavorites = inFavorites;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.popular_movie, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final PopularMovie movie = moviePosters.get(position);

        Picasso.with(mContext).load(movie.getPosterUrl()).into(holder.poster);

        holder.poster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle extras = new Bundle();
                extras.putInt("ID_INT", movie.getId());
                extras.putString("TITLE_TEXT", movie.getMovieTitle());
                extras.putString("SYNOPSIS_TEXT", movie.getSynopsis());
                extras.putString("RELEASE_TEXT", movie.getReleaseDate());
                extras.putDouble("VOTE_DOUBLE", movie.getVoteAverage());
                extras.putString("THUMBNAIL_URL", movie.getPosterUrl());
                extras.putInt("POSITION", position);
                if (Utility.alreadyInFavorites(mContext, movie.getId())) {
                    extras.putBoolean("FAVORITE", true);
                }
                if (inFavorites){
                    extras.putBoolean("IN FAVORITES", true);
                }
                FetchTrailerTask fetchTrailerTask = new FetchTrailerTask(mContext, movie.getId());
                fetchTrailerTask.execute();
                FetchReviewTask fetchReviewTask = new FetchReviewTask(mContext, movie.getId());
                fetchReviewTask.execute();

                if (MainActivity.mTwoPane) {
                    DetailActivityFragment fragment = new DetailActivityFragment();
                    fragment.setArguments(extras);
                    mContext.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.movie_detail_container, fragment, "DF_TAG")
                            .commit();
                }
                else{
                    Intent i = new Intent(mContext, DetailActivity.class);
                    i.putExtras(extras);
                    mContext.startActivity(i);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return moviePosters.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView poster;

        public ViewHolder(View view) {
            super(view);
            poster = (ImageView) view.findViewById(R.id.movie_image);
        }

    }
}





