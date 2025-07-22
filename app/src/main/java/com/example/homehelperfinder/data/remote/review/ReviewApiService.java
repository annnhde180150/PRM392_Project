package com.example.homehelperfinder.data.remote.review;

import android.content.Context;

import com.example.homehelperfinder.data.model.request.CreateReviewRequest;
import com.example.homehelperfinder.data.model.response.ReviewResponse;
import com.example.homehelperfinder.data.remote.BaseApiService;
import com.example.homehelperfinder.data.remote.RetrofitClient;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Service class for Review API operations
 */
public class ReviewApiService extends BaseApiService {
    
    private final ReviewApiInterface reviewApiInterface;
    
    public ReviewApiService() {
        this.reviewApiInterface = RetrofitClient.getReviewApiInterface();
    }
    
    /**
     * Get reviews for a specific helper
     * @param context Application context
     * @param helperId ID of the helper
     * @return CompletableFuture with list of reviews
     */
    public CompletableFuture<List<ReviewResponse>> getHelperReviews(Context context, int helperId) {
        return executeCall(
            context,
            reviewApiInterface.getHelperReviews(helperId),
            "Get Helper Reviews"
        );
    }
    
    /**
     * Get reviews for a specific helper with callback
     * @param context Application context
     * @param helperId ID of the helper
     * @param callback Callback for handling response
     */
    public void getHelperReviews(Context context, int helperId, ApiCallback<List<ReviewResponse>> callback) {
        CompletableFuture<List<ReviewResponse>> future = getHelperReviews(context, helperId);
        handleApiResponse(context, future, callback);
    }
    
    /**
     * Get reviews written by a specific user
     * @param context Application context
     * @param userId ID of the user
     * @return CompletableFuture with list of reviews
     */
    public CompletableFuture<List<ReviewResponse>> getUserReviews(Context context, int userId) {
        return executeCall(
            context,
            reviewApiInterface.getUserReviews(userId),
            "Get User Reviews"
        );
    }
    
    /**
     * Get reviews written by a specific user with callback
     * @param context Application context
     * @param userId ID of the user
     * @param callback Callback for handling response
     */
    public void getUserReviews(Context context, int userId, ApiCallback<List<ReviewResponse>> callback) {
        CompletableFuture<List<ReviewResponse>> future = getUserReviews(context, userId);
        handleApiResponse(context, future, callback);
    }
    
    /**
     * Create a new review
     * @param context Application context
     * @param request Review creation request
     * @return CompletableFuture with created review
     */
    public CompletableFuture<ReviewResponse> createReview(Context context, CreateReviewRequest request) {
        return executeCall(
            context,
            reviewApiInterface.createReview(request),
            "Create Review"
        );
    }
    
    /**
     * Create a new review with callback
     * @param context Application context
     * @param request Review creation request
     * @param callback Callback for handling response
     */
    public void createReview(Context context, CreateReviewRequest request, ApiCallback<ReviewResponse> callback) {
        CompletableFuture<ReviewResponse> future = createReview(context, request);
        handleApiResponse(context, future, callback);
    }
    
    /**
     * Create a review with individual parameters
     * @param context Application context
     * @param bookingId ID of the booking
     * @param helperId ID of the helper
     * @param rating Rating (1-5)
     * @param comment Review comment
     * @param callback Callback for handling response
     */
    public void createReview(Context context, int bookingId, int helperId, int rating, String comment, 
                           ApiCallback<ReviewResponse> callback) {
        CreateReviewRequest request = CreateReviewRequest.create(bookingId, helperId, rating, comment);
        
        // Validate request before sending
        String validationError = request.getValidationError();
        if (validationError != null) {
            if (callback != null) {
                callback.onError(validationError, new IllegalArgumentException(validationError));
            }
            return;
        }
        
        createReview(context, request, callback);
    }
}
