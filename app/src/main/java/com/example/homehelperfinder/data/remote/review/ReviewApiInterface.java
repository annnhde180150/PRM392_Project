package com.example.homehelperfinder.data.remote.review;

import com.example.homehelperfinder.data.model.request.CreateReviewRequest;
import com.example.homehelperfinder.data.model.response.ApiResponse;
import com.example.homehelperfinder.data.model.response.ReviewResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Retrofit interface for Review API endpoints
 */
public interface ReviewApiInterface {
    
    /**
     * Get all reviews for a specific helper
     * GET /api/Review/helper/{helperId}
     */
    @GET("Review/helper/{helperId}")
    Call<ApiResponse<List<ReviewResponse>>> getHelperReviews(@Path("helperId") int helperId);
    
    /**
     * Get all reviews written by a specific user
     * GET /api/Review/user/{userId}
     */
    @GET("Review/user/{userId}")
    Call<ApiResponse<List<ReviewResponse>>> getUserReviews(@Path("userId") int userId);
    
    /**
     * Create a new review
     * POST /api/Review
     */
    @POST("Review")
    Call<ApiResponse<ReviewResponse>> createReview(@Body CreateReviewRequest request);
}
