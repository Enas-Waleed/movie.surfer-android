package com.example.denzale.popularmovies;

//import android.support.v17.leanback.app;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.denzale.popularmovies.data.MoviesContract;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {


    public MainFragment() {
        // Required empty public constructor

    }


    private static final int CURSOR_LOADER_ID = 0;
    Cursor cursor;
    View rootView;
    static RecyclerView recyclerGrid;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        if (MainActivity.mTwoPane) {
            //3 columns for two-pane view
            GridLayoutManager glm = new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false);
            recyclerGrid.setLayoutManager(glm);
        }else{
            //2 columns otherwise
            GridLayoutManager glm = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
            recyclerGrid.setLayoutManager(glm);
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sort_pref = prefs.getString("sortby", "0");

        //if sort pref is not "favorites"
        if (!sort_pref.equals("2")) {

            final FetchMovieTask fetchMovieTask = new FetchMovieTask(getContext(), sort_pref);
            fetchMovieTask.execute(sort_pref);

            //if sort pref is "favorites", load from database
        } else if (sort_pref.equals("2")) {
            Cursor c = getActivity().getContentResolver().query(MoviesContract.MovieEntry.CONTENT_URI,
                    new String[]{MoviesContract.MovieEntry._ID},
                    null,
                    null,
                    null);

            if (c.getCount() > 0) {
                getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
            }
            c.close();
        }
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        recyclerGrid = (RecyclerView) rootView.findViewById(R.id.recycler_grid);

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(),
                MoviesContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursor = data;
        int[] movieIDArray = new int[cursor.getCount()];

        int count = 0;
        while (cursor.moveToNext()) {
            int movieIDIndex = cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_MOVIE_ID);
            int movieID = cursor.getInt(movieIDIndex);
            movieIDArray[count] = movieID;
            count++;
        }

        final FetchFavoritesTask fetchFavoritesTask = new FetchFavoritesTask(getContext(), movieIDArray);
        fetchFavoritesTask.execute();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}



