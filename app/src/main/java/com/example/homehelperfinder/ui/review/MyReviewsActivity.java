package com.example.homehelperfinder.ui.review;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homehelperfinder.R;
import com.example.homehelperfinder.data.model.response.ReviewResponse;
import com.example.homehelperfinder.data.repository.ReviewRepository;
import com.example.homehelperfinder.data.remote.BaseApiService;
import com.example.homehelperfinder.ui.base.BaseActivity;
import com.example.homehelperfinder.ui.review.adapter.MyReviewAdapter;
import com.example.homehelperfinder.utils.Logger;
import com.example.homehelperfinder.utils.UserManager;

import java.util.List;

/**
 * Activity for displaying user's own reviews
 */
public class MyReviewsActivity extends BaseActivity {
    
    private RecyclerView rvMyReviews;
    private LinearLayout tvEmptyState;
    private TextView tvTotalReviews;
    
    private MyReviewAdapter reviewAdapter;
    private ReviewRepository reviewRepository;
    
    private int currentUserId;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reviews);
        
        // Get current user ID
        currentUserId = UserManager.getInstance(this).getCurrentUserId();
        if (currentUserId == -1) {
            showToast("User not logged in");
            finish();
            return;
        }
        
        initViews();
        setupRecyclerView();
        setupMenuNavigation();
        
        reviewRepository = new ReviewRepository();
        
        loadMyReviews();
    }
    
    private void initViews() {
        rvMyReviews = findViewById(R.id.rv_my_reviews);
        tvEmptyState = findViewById(R.id.tv_empty_state);
        tvTotalReviews = findViewById(R.id.tv_total_reviews);
        
        // Setup back button
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
    }
    
    private void setupRecyclerView() {
        reviewAdapter = new MyReviewAdapter(this);
        rvMyReviews.setLayoutManager(new LinearLayoutManager(this));
        rvMyReviews.setAdapter(reviewAdapter);
        
        // Set click listener for review items
        reviewAdapter.setOnReviewClickListener((review, position) -> {
            // Handle review click if needed (e.g., edit review, view helper)
            Logger.d("MyReviewsActivity", "My review clicked: " + review.getReviewId());
        });
    }
    
    private void loadMyReviews() {
        showProgressDialog("Loading your reviews...");
        
        reviewRepository.getUserReviews(this, currentUserId, new BaseApiService.ApiCallback<List<ReviewResponse>>() {
            @Override
            public void onSuccess(List<ReviewResponse> reviews) {
                hideProgressDialog();
                handleReviewsLoaded(reviews);
            }
            
            @Override
            public void onError(String errorMessage, Throwable throwable) {
                hideProgressDialog();
                Logger.e("MyReviewsActivity", "Error loading my reviews: " + errorMessage, throwable);
                showToast("Failed to load reviews: " + errorMessage);
                showEmptyState();
            }
        });
    }
    
    private void handleReviewsLoaded(List<ReviewResponse> reviews) {
        if (reviews == null || reviews.isEmpty()) {
            showEmptyState();
        } else {
            showReviews(reviews);
            updateReviewCount(reviews.size());
        }
    }
    
    private void showReviews(List<ReviewResponse> reviews) {
        rvMyReviews.setVisibility(View.VISIBLE);
        tvEmptyState.setVisibility(View.GONE);
        tvTotalReviews.setVisibility(View.VISIBLE);
        
        reviewAdapter.updateReviews(reviews);
    }
    
    private void showEmptyState() {
        rvMyReviews.setVisibility(View.GONE);
        tvEmptyState.setVisibility(View.VISIBLE);
        tvTotalReviews.setVisibility(View.GONE);
    }
    
    private void updateReviewCount(int count) {
        String reviewsText = count == 1 ? "review" : "reviews";
        tvTotalReviews.setText("You have written " + count + " " + reviewsText);
    }
    
    /**
     * Refresh reviews data
     */
    public void refreshReviews() {
        loadMyReviews();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // Refresh reviews when returning to this activity
        if (reviewAdapter != null && !reviewAdapter.isEmpty()) {
            refreshReviews();
        }
    }
}
