package com.fypproject_2022.e_agriculture_app.Models;

import java.io.Serializable;

public class Review implements Serializable {

    float rating;
    String productId;
    String reviewerName;
    String description;

    public Review() {
    }

    public Review(float rating, String productId, String reviewerName, String description) {
        this.rating = rating;
        this.productId = productId;
        this.reviewerName = reviewerName;
        this.description = description;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
