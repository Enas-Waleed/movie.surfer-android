package com.example.denzale.popularmovies;

import android.content.Context;
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

/**
 * Created by Denzale on 2/14/2016.
 */
public class FetchReviewTask extends AsyncTask<String, Void, PopularMovie[]> {

    private Context mContext;
    private int movieID;
    static PopularMovie[] movies_with_reviews;
    static int numReviews;


    public FetchReviewTask(Context context, int movieID){
        mContext = context;
        this.movieID = movieID;

    }

    private PopularMovie[] getReviewsFromJson(String reviewsJsonStr)
            throws JSONException {

        final String MBD_REVIEWS_RESULTS = "results";
        final String MBD_REVIEWS_CONTENT = "content";
        final String MBD_REVIEW_AUTHOR = "author";
        final String MBD_REVIEWS_URL = "url";

        String review;
        String review_author;
        String review_url;
        PopularMovie movie_with_review;

        JSONObject reviewJson = new JSONObject(reviewsJsonStr);
        JSONArray reviewArray = reviewJson.getJSONArray(MBD_REVIEWS_RESULTS);
        movies_with_reviews = new PopularMovie[reviewArray.length()];

        JSONObject reviewInfo;

        for (int i = 0; i < reviewArray.length(); i++) {

            reviewInfo = reviewArray.getJSONObject(i);
            review = reviewInfo.getString(MBD_REVIEWS_CONTENT);
            review_author = reviewInfo.getString(MBD_REVIEW_AUTHOR);
            review_url = reviewInfo.getString(MBD_REVIEWS_URL);
            movie_with_review = new PopularMovie(review_author, review, review_url);
            movies_with_reviews[i] = movie_with_review;

        }

        numReviews = movies_with_reviews.length;
        return movies_with_reviews;

    }

    @Override
    protected PopularMovie[] doInBackground(String... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        //Will contain raw JSON as a sting
        String reviewsJsonStr = null;

        String apiKey = "c31191ceb90fed5378d03ca7d63d0f21";

        try

        {
            //Construct the URL for The Movie Database query

            URL url = new URL("https://api.themoviedb.org/3/movie/" + movieID + "/reviews?api_key=" + apiKey);

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

            reviewsJsonStr = buffer.toString();

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
            return getReviewsFromJson(reviewsJsonStr);
        } catch (JSONException e){
            Log.e("MainFragment", e.getMessage(), e);
            e.printStackTrace();
        }

        //This will happen if there was an error getting or parsing the movie data
        return null;
    }

}