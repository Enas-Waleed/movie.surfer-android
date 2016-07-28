package com.example.denzale.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
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
 * Created by Denzale on 2/4/2016.
 */
public class FetchMovieTask extends AsyncTask<String, Void, PopularMovie[]> {

    private Context mContext;
    private Activity context_activity = (Activity) mContext;
    private static int numMovies = 18;
    public PopularMovieRecyclerViewAdapter popularMovieRecyclerViewAdapter;
    static FetchTrailerTask fetchTrailerTask;
    static FetchReviewTask fetchReviewTask;


    PopularMovie[] popularMovies = new PopularMovie[numMovies];
    List<PopularMovie> movieList = new LinkedList<PopularMovie>(Arrays.asList(popularMovies));

    public FetchMovieTask(Context context, String sort_pref){
        mContext = context;
    }

    private final String LOG_TAG = FetchMovieTask.class.getSimpleName();

    private PopularMovie[] getMovieDataFromJson(String movieJsonStr, int numMovies)
            throws JSONException {

        final String baseUrl = "http://image.tmdb.org/t/p/w342/";
        final String MBD_RESULTS = "results";
        final String MDB_PATH = "poster_path";
        final String MBD_TITLE = "original_title";
        final String MBD_SYNOPSIS = "overview";
        final String MBD_RELEASE = "release_date";
        final String MBD_VOTE = "vote_average";



        JSONObject movieJson = new JSONObject(movieJsonStr);
        JSONArray movieArray = movieJson.getJSONArray(MBD_RESULTS);


        for (int i = 0; i < numMovies; i++){

            String posterPath;
            String posterImgUrl;
            String movie_title;
            String synopsis;
            String releaseDate;
            int id;
            double voteAverage;

            JSONObject movieInfo = movieArray.getJSONObject(i);

            posterPath = movieInfo.getString(MDB_PATH);
            posterImgUrl = baseUrl + posterPath;
            movie_title = movieInfo.getString(MBD_TITLE);
            synopsis = movieInfo.getString(MBD_SYNOPSIS);
            releaseDate = movieInfo.getString(MBD_RELEASE);
            voteAverage = movieInfo.getDouble(MBD_VOTE);
            id = movieInfo.getInt("id");


            PopularMovie popularMovie = new PopularMovie(id, posterImgUrl, movie_title, synopsis, releaseDate, voteAverage);
            popularMovies[i] = popularMovie;
        }

        return popularMovies;


    }

    @Override
    protected PopularMovie[] doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            //Will contain raw JSON as a sting
            String movieJsonStr = null;

            String apiKey = "c31191ceb90fed5378d03ca7d63d0f21";

            try

            {
                //Construct the URL for The Movie Database query

                final String SORT_PARAM = "sort_by";
                final String APIKEY_PARAM = "api_key";


                Uri.Builder uri = new Uri.Builder();
                uri.scheme("http");
                uri.authority("api.themoviedb.org");
                uri.appendPath("3");
                uri.appendPath("discover");
                uri.appendPath("movie");
                if (params[0].equals("0")) {
                    uri.appendQueryParameter(SORT_PARAM, "popularity.desc");
                } else if (params[0].equals("1")) {
                    uri.appendQueryParameter(SORT_PARAM, "vote_average.desc");
                }
                uri.appendQueryParameter(APIKEY_PARAM, apiKey);

                URL url = new URL(uri.build().toString());


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
                return getMovieDataFromJson(movieJsonStr, numMovies);
            } catch (JSONException e) {
                Log.e("MainFragment", e.getMessage(), e);
                e.printStackTrace();
            }

            //This will happen if there was an error getting or parsing the movie data

            return null;
        }



    @Override
    protected void onPostExecute(PopularMovie[] popMovies) {
        if (popMovies != null){

            movieList.clear();

            for (int i = 0; i < popMovies.length; i++){

                PopularMovie popMovie;
                popMovie = new PopularMovie(
                        popMovies[i].getId(),
                        popMovies[i].getPosterUrl(),
                        popMovies[i].getMovieTitle(),
                        popMovies[i].getSynopsis(),
                        popMovies[i].getReleaseDate(),
                        popMovies[i].getVoteAverage()
                );

                movieList.add(popMovie);
            }
        }

        popularMovieRecyclerViewAdapter = new PopularMovieRecyclerViewAdapter(mContext, movieList, false);
        MainFragment.recyclerGrid.setAdapter(popularMovieRecyclerViewAdapter);
    }
}