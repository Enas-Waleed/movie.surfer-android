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
 * Created by Denzale on 2/13/2016.
 */
public class FetchTrailerTask extends AsyncTask<String, Void, String[]> {

    private Context mContext;
    private int movieID;
    static String[] youtubeKeys;
    static int numTrailers;


    public FetchTrailerTask(Context context, int movieID){
        mContext = context;
        this.movieID = movieID;

    }

    private String[] getTrailersFromJson(String trailerJsonStr)
            throws JSONException {

        final String MBD_RESULTS = "results";
        final String MBD_YOUTUBE_KEY = "key";

        String youtubeKey;

        JSONObject videoJson = new JSONObject(trailerJsonStr);
        JSONArray videoArray = videoJson.getJSONArray(MBD_RESULTS);
        youtubeKeys = new String[videoArray.length()];

        JSONObject trailerInfo;

        for (int i = 0; i < videoArray.length(); i++) {

                trailerInfo = videoArray.getJSONObject(i);
                youtubeKey = trailerInfo.getString(MBD_YOUTUBE_KEY);
                youtubeKeys[i] = youtubeKey;

        }

        numTrailers = youtubeKeys.length;
        return youtubeKeys;

    }

    @Override
    protected String[] doInBackground(String... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        //Will contain raw JSON as a sting
        String trailerJsonStr = null;

        String apiKey = "c31191ceb90fed5378d03ca7d63d0f21";

        try

        {
            //Construct the URL for The Movie Database query

            URL url = new URL("https://api.themoviedb.org/3/movie/" + movieID + "/videos?api_key=" + apiKey);

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

            trailerJsonStr = buffer.toString();

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
            return getTrailersFromJson(trailerJsonStr);
        } catch (JSONException e){
            Log.e("MainFragment", e.getMessage(), e);
            e.printStackTrace();
        }

        //This will happen if there was an error getting or parsing the movie data

        return null;
    }


}