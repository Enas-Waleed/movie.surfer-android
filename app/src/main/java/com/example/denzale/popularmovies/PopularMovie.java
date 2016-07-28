package com.example.denzale.popularmovies;

/**
 * Created by Denzale on 11/9/2015.
 */
public class PopularMovie {

    String posterUrl;
    String movieTitle;
    String synopsis;
    String releaseDate;
    double voteAverage;
    int id;
    String author;
    String review;
    String reviewUrl;




    public PopularMovie(int id, String posterUrl, String movieTitle, String synopsis, String releaseDate, double voteAverage) {
        this.posterUrl = posterUrl;
        this.movieTitle = movieTitle;
        this.synopsis = synopsis;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.id = id;

    }

    //for review activity
    public PopularMovie(String author, String review, String reviewUrl){
        this.author = author;
        this.review = review;
        this.reviewUrl = reviewUrl;
    }


    public String getPosterUrl() {
        return posterUrl;
    }

    public String getMovieTitle(){
        return movieTitle;
    }

    public String getSynopsis(){
        return synopsis;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public int getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getReview() {
        return review;
    }

    public String getReviewUrl() {
        return reviewUrl;
    }
}
