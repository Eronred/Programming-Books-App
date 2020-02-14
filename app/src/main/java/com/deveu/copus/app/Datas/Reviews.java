package com.deveu.copus.app.Datas;

public class Reviews {
    private String usid;
    private String bookid;
    private String rating;
    private String reviewid;
    private String review;
    private String date;

    public Reviews() {
    }

    public Reviews(String usid, String bookid, String rating, String reviewid, String review, String date) {
        this.usid = usid;
        this.bookid = bookid;
        this.rating = rating;
        this.reviewid = reviewid;
        this.review = review;
        this.date = date;
    }

    public String getUsid() {
        return usid;
    }

    public void setUsid(String usid) {
        this.usid = usid;
    }

    public String getBookid() {
        return bookid;
    }

    public void setBookid(String bookid) {
        this.bookid = bookid;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReviewid() {
        return reviewid;
    }

    public void setReviewid(String reviewid) {
        this.reviewid = reviewid;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
