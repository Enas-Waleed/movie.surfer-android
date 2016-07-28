package com.example.denzale.popularmovies;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


public class ReviewFragment extends Fragment {

    PopularMovie[] mReviewsArray;


    public ReviewFragment() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_review, container, false);

        TextView movieTitle = (TextView) view.findViewById(R.id.movie_title);

        movieTitle.setText(getActivity().getIntent().getStringExtra("MOVIE_TITLE"));

        mReviewsArray =   FetchMovieTask.fetchReviewTask.movies_with_reviews;

        List<PopularMovie> reviewsList = new LinkedList<PopularMovie>(Arrays.asList(mReviewsArray));

        ReviewRecyclerViewAdapter reviewAdapter = new ReviewRecyclerViewAdapter(reviewsList);

        // Set the adapter
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager llm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(reviewAdapter);

        return view;
    }

}
