package com.example.denzale.popularmovies;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


public class ReviewRecyclerViewAdapter extends RecyclerView.Adapter<ReviewRecyclerViewAdapter.ViewHolder> {

    private final List<PopularMovie> reviews;


    public ReviewRecyclerViewAdapter(List<PopularMovie> reviews) {
        this.reviews = reviews;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        PopularMovie review = reviews.get(position);
        Spanned author = Html.fromHtml("<b>" + "Author: " + "<b>");
        Spanned authorName = Html.fromHtml("<i>" + review.getAuthor() + "<i>");
        holder.authorText.setText(TextUtils.concat(author, authorName));
        holder.reviewText.setText(review.getReview());
        holder.urlText.setText(review.getReviewUrl());

    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView authorText;
        public TextView reviewText;
        public TextView urlText;

        public ViewHolder(View view) {
            super(view);
            authorText = (TextView) view.findViewById(R.id.author_text);
            reviewText = (TextView) view.findViewById(R.id.review_text);
            urlText = (TextView) view.findViewById(R.id.url_text);
        }
    }
}
