package com.example.denzale.popularmovies;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Denzale on 2/20/2016.
 */
public class FetchFavoritesTask extends AsyncTask<String, Void, PopularMovie[]> {

    private Context mContext;
    private int[] movieIDArray;
    private PopularMovie[] mFavoriteMovies;
    List<PopularMovie> favoriteMovieList;
    static PopularMovieRecyclerViewAdapter favoriteMovieAdapter;

    public FetchFavoritesTask(Context context, int[] movieIDArray) {
        this.mContext = context;
        this.movieIDArray = movieIDArray;
    }

    private PopularMovie getMovieDataFromJson(String movieJsonStr)
            throws JSONException {

        final String baseUrl = "http://image.tmdb.org/t/p/w342/";
        final String MDB_PATH = "poster_path";
        final String MBD_TITLE = "original_title";
        final String MBD_SYNOPSIS = "overview";
        final String MBD_RELEASE = "release_date";
        final String MBD_VOTE = "vote_average";


        JSONObject movieInfo = new JSONObject(movieJsonStr);


        String posterPath;
        String posterImgUrl;
        String movie_title;
        String synopsis;
        String releaseDate;
        int id;
        double voteAverage;

        ;
        posterPath = movieInfo.getString(MDB_PATH);
        posterImgUrl = baseUrl + posterPath;
        movie_title = movieInfo.getString(MBD_TITLE);
        synopsis = movieInfo.getString(MBD_SYNOPSIS);
        releaseDate = movieInfo.getString(MBD_RELEASE);
        voteAverage = movieInfo.getDouble(MBD_VOTE);
        id = movieInfo.getInt("id");


        PopularMovie favoriteMovie = new PopularMovie(id, posterImgUrl, movie_title, synopsis, releaseDate, voteAverage);


        return favoriteMovie;
    }

    private PopularMovie getMovieFromApi(int movieID){
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        //Will contain raw JSON as a sting
        String movieJsonStr = null;

        String apiKey = "c31191ceb90fed5378d03ca7d63d0f21";

        try

        {
            //Construct the URL for The Movie Database query

            URL url = new URL("https://api.themoviedb.org/3/movie/" + movieID + "?api_key=" + apiKey);

            //Create the request to The Movie Database, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            //Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                //Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;

            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                //Stream was empty, so you don't have to parse
                return null;
            }

            movieJsonStr = buffer.toString();

        } catch (IOException e)

        {
            Log.e("MainFragment", "Error", e);
            //If we didn't get the popular movie data, we have nothing to parse
            return null;
        } finally

        {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("MainFragment", "Error closing stream", e);
                }

            }
        }

        try {
           return getMovieDataFromJson(movieJsonStr);

        } catch (JSONException e) {
            Log.e("MainFragment", e.getMessage(), e);
            e.printStackTrace();
        }

        //This will happen if there was an error getting or parsing the movie
        return null;
    }



    @Override
    protected PopularMovie[] doInBackground(String... params) {
        mFavoriteMovies = new PopularMovie[movieIDArray.length];

        for (int i = 0; i < movieIDArray.length; i++) {
            mFavoriteMovies[i] = getMovieFromApi(movieIDArray[i]);
        }
        return mFavoriteMovies;
    }

    @Override
    protected void onPostExecute(PopularMovie[] favoriteMovies) {
        favoriteMovies = mFavoriteMovies;
        if (favoriteMovies[0] != null) {
            favoriteMovieList = new LinkedList<PopularMovie>(Arrays.asList(favoriteMovies));
            favoriteMovieAdapter = new PopularMovieRecyclerViewAdapter(mContext, favoriteMovieList, true);
            MainFragment.recyclerGrid.setAdapter(favoriteMovieAdapter);
        }
    }
}

