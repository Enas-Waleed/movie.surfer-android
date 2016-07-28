package com.example.denzale.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.denzale.popularmovies.data.MoviesContract;
import com.example.denzale.popularmovies.data.MoviesDBHelper;

/**
 * Created by Denzale on 2/21/2016.
 */
public class Utility {


    //checks if a movie is already in the favorites database
    public static boolean alreadyInFavorites(Context context, int movieID){

        MoviesDBHelper moviesDBHelper = new MoviesDBHelper(context);
        SQLiteDatabase database = moviesDBHelper.getReadableDatabase();
        String query = "SELECT * FROM " + MoviesContract.MovieEntry.TABLE_MOVIES + " WHERE " + MoviesContract.MovieEntry.COLUMN_MOVIE_ID + " = " + movieID;

        Cursor cursor = database.rawQuery(query, null);
        int i = cursor.getCount();
        if (i <= 0){
            return false;
        }
        else {
            return true;
        }
    }

    //creates trailer buttons based on the number of trailers the movie has in the API
    public static void createTrailerButton(final Context context, ViewGroup parent, final int numTrailers) {

        for (int i = 0; i < numTrailers; i++) {
            final int trailerID = i;

            RelativeLayout relativeLayout = new RelativeLayout(context);
            RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            relativeLayout.setLayoutParams(rlp);

            final ImageView trailerButton = new ImageView(context);
            trailerButton.setImageResource(R.drawable.play);
            trailerButton.setId(400 + trailerID);
            RelativeLayout.LayoutParams ilp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            ilp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            ilp.setMargins(0, 0, 24, 24);
            trailerButton.setLayoutParams(ilp);
            trailerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    trailerButton.setColorFilter(context.getResources().getColor(R.color.red));
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + FetchMovieTask.fetchTrailerTask.youtubeKeys[trailerID])));
                }
            });
            relativeLayout.addView(trailerButton);

            TextView trailerText = new TextView(context);
            RelativeLayout.LayoutParams tlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            tlp.addRule(RelativeLayout.RIGHT_OF, trailerButton.getId());
            tlp.addRule(RelativeLayout.CENTER_IN_PARENT);
            trailerText.setText("Trailer " + (trailerID + 1));
            trailerText.setTextColor(context.getResources().getColor(R.color.black));
            trailerText.setLayoutParams(tlp);
            relativeLayout.addView(trailerText);

            parent.addView(relativeLayout);

        }

    }
}
