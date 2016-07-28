package com.example.denzale.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.denzale.popularmovies.data.MoviesContract;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    public DetailActivityFragment() {
    }

    final int numTrailers = FetchMovieTask.fetchTrailerTask.numTrailers;
    int movieID;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {


            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
            LinearLayout ll = (LinearLayout) rootView.findViewById(R.id.detail_linear_layout);
            final TextView movie_title = (TextView) rootView.findViewById(R.id.movie_title);
            final TextView movie_synopsis = (TextView) rootView.findViewById(R.id.movie_synopsis);
            TextView movie_release = (TextView) rootView.findViewById(R.id.movie_release);
            TextView movie_vote = (TextView) rootView.findViewById(R.id.movie_vote);
            ImageView movie_thumbnail = (ImageView) rootView.findViewById(R.id.movie_thumbnail);
            Button review_button = (Button) rootView.findViewById(R.id.review_button);
            final MaterialFavoriteButton star_button = (MaterialFavoriteButton) rootView.findViewById(R.id.star_button);

            if (!MainActivity.mTwoPane) {

                Intent i = getActivity().getIntent();
                final Bundle extras = i.getExtras();
                movie_title.setText(extras.getString("TITLE_TEXT"));
                movie_synopsis.setText(extras.getString("SYNOPSIS_TEXT"));
                movie_release.setText(extras.getString("RELEASE_TEXT"));
                double vote = extras.getDouble("VOTE_DOUBLE", 0.0);
                movie_vote.setText(Math.round(vote) + "/10");
                Picasso.with(this.getContext()).load(extras.getString("THUMBNAIL_URL")).into(movie_thumbnail);
                movieID = extras.getInt("ID_INT", 0);


                review_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), ReviewActivity.class);
                        intent.putExtra("MOVIE_TITLE", (String) movie_title.getText());
                        startActivity(intent);
                    }
                });

                //if this movie is favorited, or the current sort mode is "Favorites"
                if (i.hasExtra("FAVORITE") || i.hasExtra("IN FAVORITES")) {
                    //favorite button should already show in favorited state
                    star_button.toggleFavorite(true);
                    star_button.setOnFavoriteChangeListener(new MaterialFavoriteButton.OnFavoriteChangeListener() {
                        @Override
                        public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                            if (favorite) {
                                ContentValues value = new ContentValues();
                                value.put(MoviesContract.MovieEntry.COLUMN_MOVIE_ID, movieID);
                                getActivity().getContentResolver().insert(MoviesContract.MovieEntry.CONTENT_URI, value);
                                Toast toast = Toast.makeText(getContext(), "Added to your favorites", Toast.LENGTH_SHORT);
                                toast.show();

                            } else if (!favorite) {
                                getActivity().getContentResolver().delete(MoviesContract.MovieEntry.CONTENT_URI, MoviesContract.MovieEntry.COLUMN_MOVIE_ID + "=?", new String[]{"" + movieID});
                                Toast toast = Toast.makeText(getContext(), "Removed from favorites", Toast.LENGTH_SHORT);
                                toast.show();
                            }

                        }
                    });

                } else if (!i.hasExtra("IN FAVORITES") && !i.hasExtra("FAVORITE")) {
                    star_button.setOnFavoriteChangeListener(new MaterialFavoriteButton.OnFavoriteChangeListener() {
                        @Override
                        public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                            //if user clicks star
                            if (favorite) {
                                ContentValues value = new ContentValues();
                                value.put(MoviesContract.MovieEntry.COLUMN_MOVIE_ID, movieID);
                                getActivity().getContentResolver().insert(MoviesContract.MovieEntry.CONTENT_URI, value);
                                Toast toast = Toast.makeText(getContext(), "Added to your favorites", Toast.LENGTH_SHORT);
                                toast.show();
                                //if user un-clicks star
                            } else if (!favorite) {
                                getActivity().getContentResolver().delete(MoviesContract.MovieEntry.CONTENT_URI, MoviesContract.MovieEntry.COLUMN_MOVIE_ID + "=?", new String[]{"" + movieID});
                                Toast toast = Toast.makeText(getContext(), "Removed from favorites", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }
                    });
                }

                Utility.createTrailerButton(getContext(), ll, numTrailers);
            } else {

                movie_title.setText(getArguments().getString("TITLE_TEXT"));
                movie_synopsis.setText(getArguments().getString("SYNOPSIS_TEXT"));
                movie_release.setText(getArguments().getString("RELEASE_TEXT"));
                double vote = getArguments().getDouble("VOTE_DOUBLE", 0.0);
                movie_vote.setText(Math.round(vote) + "/10");
                Picasso.with(this.getContext()).load(getArguments().getString("THUMBNAIL_URL")).into(movie_thumbnail);
                movieID = getArguments().getInt("ID_INT", 0);

                review_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle title = new Bundle();
                        title.putString("MOVIE_STRING", (String) movie_title.getText());
                        ReviewFragment reviewFragment = new ReviewFragment();
                        reviewFragment.setArguments(title);
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.movie_detail_container, reviewFragment);
                        // ft.addToBackStack("DF_TAG");
                        ft.commit();

                    }
                });

                if (getArguments().containsKey("FAVORITE") && !getArguments().containsKey("IN FAVORITES")) {
                    star_button.toggleFavorite(true);
                    star_button.setOnFavoriteChangeListener(new MaterialFavoriteButton.OnFavoriteChangeListener() {
                        @Override
                        public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                            if (favorite) {
                                ContentValues value = new ContentValues();
                                value.put(MoviesContract.MovieEntry.COLUMN_MOVIE_ID, movieID);
                                getActivity().getContentResolver().insert(MoviesContract.MovieEntry.CONTENT_URI, value);
                                Toast toast = Toast.makeText(getContext(), "Added to your favorites", Toast.LENGTH_SHORT);
                                toast.show();

                            } else if (!favorite) {
                                getActivity().getContentResolver().delete(MoviesContract.MovieEntry.CONTENT_URI, MoviesContract.MovieEntry.COLUMN_MOVIE_ID + "=?", new String[]{"" + movieID});
                                Toast toast = Toast.makeText(getContext(), "Removed from favorites", Toast.LENGTH_SHORT);
                                toast.show();
                            }

                        }
                    });
                } else if (getArguments().containsKey("IN FAVORITES")) {
                    star_button.toggleFavorite(true);
                    star_button.setClickable(false);

                } else if (!getArguments().containsKey("IN FAVORITES") && !getArguments().containsKey("FAVORITE")) {
                    star_button.setOnFavoriteChangeListener(new MaterialFavoriteButton.OnFavoriteChangeListener() {
                        @Override
                        public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                            if (favorite) {
                                ContentValues value = new ContentValues();
                                value.put(MoviesContract.MovieEntry.COLUMN_MOVIE_ID, movieID);
                                getActivity().getContentResolver().insert(MoviesContract.MovieEntry.CONTENT_URI, value);
                                Toast toast = Toast.makeText(getContext(), "Added to your favorites", Toast.LENGTH_SHORT);
                                toast.show();

                            } else if (!favorite) {
                                getActivity().getContentResolver().delete(MoviesContract.MovieEntry.CONTENT_URI, MoviesContract.MovieEntry.COLUMN_MOVIE_ID + "=?", new String[]{"" + movieID});
                                Toast toast = Toast.makeText(getContext(), "Removed from favorites", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }
                    });
                }

                Utility.createTrailerButton(getContext(), ll, numTrailers);
            }


        return rootView;
    }


}
