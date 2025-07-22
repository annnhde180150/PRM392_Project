package com.example.homehelperfinder.data.repository;

import android.content.Context;

import com.example.homehelperfinder.data.model.request.CreateReviewRequest;
import com.example.homehelperfinder.data.model.response.ReviewResponse;
import com.example.homehelperfinder.data.remote.BaseApiService;
import com.example.homehelperfinder.data.remote.review.ReviewApiService;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Repository for review data operations
 */
public class ReviewRepository {
    
    private final ReviewApiService reviewApiService;
    
    public ReviewRepository() {
        this.reviewApiService = new ReviewApiService();
    }
    
    // CompletableFuture methods for helper reviews
    public CompletableFuture<List<ReviewResponse>> getHelperReviews(Context context, int helperId) {
        return reviewApiService.getHelperReviews(context, helperId);
    }
    
    // Callback methods for helper reviews
    public void getHelperReviews(Context context, int helperId, BaseApiService.ApiCallback<List<ReviewResponse>> callback) {
        reviewApiService.getHelperReviews(context, helperId, callback);
    }
    
    // CompletableFuture methods for user reviews
    public CompletableFuture<List<ReviewResponse>> getUserReviews(Context context, int userId) {
        return reviewApiService.getUserReviews(context, userId);
    }
    
    // Callback methods for user reviews
    public void getUserReviews(Context context, int userId, BaseApiService.ApiCallback<List<ReviewResponse>> callback) {
        reviewApiService.getUserReviews(context, userId, callback);
    }
    
    // CompletableFuture methods for creating reviews
    public CompletableFuture<ReviewResponse> createReview(Context context, CreateReviewRequest request) {
        return reviewApiService.createReview(context, request);
    }
    
    // Callback methods for creating reviews
    public void createReview(Context context, CreateReviewRequest request, BaseApiService.ApiCallback<ReviewResponse> callback) {
        reviewApiService.createReview(context, request, callback);
    }
    
    // Convenience method for creating reviews with individual parameters
    public void createReview(Context context, int bookingId, int helperId, int rating, String comment, 
                           BaseApiService.ApiCallback<ReviewResponse> callback) {
        reviewApiService.createReview(context, bookingId, helperId, rating, comment, callback);
    }
    
    /**
     * Calculate average rating from a list of reviews
     */
    public static double calculateAverageRating(List<ReviewResponse> reviews) {
        if (reviews == null || reviews.isEmpty()) {
            return 0.0;
        }
        
        double totalRating = 0.0;
        int validReviews = 0;
        
        for (ReviewResponse review : reviews) {
            if (review.isValidRating()) {
                totalRating += review.getRating();
                validReviews++;
            }
        }
        
        return validReviews > 0 ? totalRating / validReviews : 0.0;
    }
    
    /**
     * Get rating distribution from a list of reviews
     */
    public static int[] getRatingDistribution(List<ReviewResponse> reviews) {
        int[] distribution = new int[5]; // Index 0 = 1 star, Index 4 = 5 stars
        
        if (reviews != null) {
            for (ReviewResponse review : reviews) {
                if (review.isValidRating()) {
                    distribution[review.getRating() - 1]++;
                }
            }
        }
        
        return distribution;
    }
    
    /**
     * Get total number of valid reviews
     */
    public static int getTotalReviewCount(List<ReviewResponse> reviews) {
        if (reviews == null) {
            return 0;
        }
        
        int count = 0;
        for (ReviewResponse review : reviews) {
            if (review.isValidRating()) {
                count++;
            }
        }
        
        return count;
    }
}
