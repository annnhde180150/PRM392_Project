package com.example.homehelperfinder.data.model.request;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request model for creating a new review
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateReviewRequest {
    
    @SerializedName("bookingId")
    private int bookingId;
    
    @SerializedName("helperId")
    private int helperId;
    
    @SerializedName("rating")
    private int rating;
    
    @SerializedName("comment")
    private String comment;
    
    /**
     * Validate the review request
     */
    public boolean isValid() {
        return bookingId > 0 && 
               helperId > 0 && 
               rating >= 1 && 
               rating <= 5 &&
               comment != null && 
               !comment.trim().isEmpty();
    }
    
    /**
     * Get validation error message
     */
    public String getValidationError() {
        if (bookingId <= 0) {
            return "Invalid booking ID";
        }
        if (helperId <= 0) {
            return "Invalid helper ID";
        }
        if (rating < 1 || rating > 5) {
            return "Rating must be between 1 and 5 stars";
        }
        if (comment == null || comment.trim().isEmpty()) {
            return "Comment is required";
        }
        if (comment.trim().length() < 10) {
            return "Comment must be at least 10 characters long";
        }
        if (comment.trim().length() > 500) {
            return "Comment must be less than 500 characters";
        }
        return null;
    }
    
    /**
     * Clean and prepare the comment
     */
    public void prepareComment() {
        if (comment != null) {
            comment = comment.trim();
        }
    }
    
    /**
     * Create a review request with validation
     */
    public static CreateReviewRequest create(int bookingId, int helperId, int rating, String comment) {
        CreateReviewRequest request = new CreateReviewRequest(bookingId, helperId, rating, comment);
        request.prepareComment();
        return request;
    }
}
