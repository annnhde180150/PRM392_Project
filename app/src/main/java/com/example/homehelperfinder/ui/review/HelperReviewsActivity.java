package com.example.homehelperfinder.ui.review;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.RatingBar;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homehelperfinder.R;
import com.example.homehelperfinder.data.model.response.ReviewResponse;
import com.example.homehelperfinder.data.repository.ReviewRepository;
import com.example.homehelperfinder.data.remote.BaseApiService;
import com.example.homehelperfinder.ui.base.BaseActivity;
import com.example.homehelperfinder.ui.review.adapter.ReviewAdapter;
import com.example.homehelperfinder.utils.Logger;

import java.util.List;

/**
 * Activity for displaying reviews of a specific helper
 */
public class HelperReviewsActivity extends BaseActivity {
    
    public static final String EXTRA_HELPER_ID = "helper_id";
    public static final String EXTRA_HELPER_NAME = "helper_name";
    
    private RecyclerView rvReviews;
    private LinearLayout tvEmptyState;
    private TextView tvHelperName;
    private TextView tvAverageRating;
    private TextView tvTotalReviews;
    private RatingBar rbAverageRating;
    private View layoutRatingInfo;
    
    private ReviewAdapter reviewAdapter;
    private ReviewRepository reviewRepository;
    
    private int helperId;
    private String helperName;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helper_reviews);
        
        // Get data from intent
        Intent intent = getIntent();
        helperId = intent.getIntExtra(EXTRA_HELPER_ID, -1);
        helperName = intent.getStringExtra(EXTRA_HELPER_NAME);
        
        if (helperId == -1) {
            showToast("Invalid helper ID");
            finish();
            return;
        }
        
        initViews();
        setupRecyclerView();
        setupMenuNavigation();
        
        reviewRepository = new ReviewRepository();
        
        loadHelperReviews();
    }
    
    private void initViews() {
        rvReviews = findViewById(R.id.rv_reviews);
        tvEmptyState = findViewById(R.id.tv_empty_state);
        tvHelperName = findViewById(R.id.tv_helper_name);
        tvAverageRating = findViewById(R.id.tv_average_rating);
        tvTotalReviews = findViewById(R.id.tv_total_reviews);
        rbAverageRating = findViewById(R.id.rb_average_rating);
        layoutRatingInfo = findViewById(R.id.layout_rating_info);
        
        // Set helper name
        if (helperName != null && !helperName.isEmpty()) {
            tvHelperName.setText(helperName + "'s Reviews");
        } else {
            tvHelperName.setText("Helper Reviews");
        }
        
        // Setup back button
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
    }
    
    private void setupRecyclerView() {
        reviewAdapter = new ReviewAdapter(this);
        rvReviews.setLayoutManager(new LinearLayoutManager(this));
        rvReviews.setAdapter(reviewAdapter);
        
        // Set click listener for review items
        reviewAdapter.setOnReviewClickListener((review, position) -> {
            // Handle review click if needed (e.g., show full review dialog)
            Logger.d("HelperReviewsActivity", "Review clicked: " + review.getReviewId());
        });
    }
    
    private void loadHelperReviews() {
        showProgressDialog("Loading reviews...");
        
        reviewRepository.getHelperReviews(this, helperId, new BaseApiService.ApiCallback<List<ReviewResponse>>() {
            @Override
            public void onSuccess(List<ReviewResponse> reviews) {
                hideProgressDialog();
                handleReviewsLoaded(reviews);
            }
            
            @Override
            public void onError(String errorMessage, Throwable throwable) {
                hideProgressDialog();
                Logger.e("HelperReviewsActivity", "Error loading reviews: " + errorMessage, throwable);
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
            updateRatingInfo(reviews);
        }
    }
    
    private void showReviews(List<ReviewResponse> reviews) {
        rvReviews.setVisibility(View.VISIBLE);
        tvEmptyState.setVisibility(View.GONE);
        layoutRatingInfo.setVisibility(View.VISIBLE);
        
        reviewAdapter.updateReviews(reviews);
    }
    
    private void showEmptyState() {
        rvReviews.setVisibility(View.GONE);
        tvEmptyState.setVisibility(View.VISIBLE);
        layoutRatingInfo.setVisibility(View.GONE);
    }
    
    private void updateRatingInfo(List<ReviewResponse> reviews) {
        double averageRating = ReviewRepository.calculateAverageRating(reviews);
        int totalReviews = ReviewRepository.getTotalReviewCount(reviews);
        
        // Update average rating
        rbAverageRating.setRating((float) averageRating);
        tvAverageRating.setText(String.format("%.1f", averageRating));
        
        // Update total reviews count
        String reviewsText = totalReviews == 1 ? "review" : "reviews";
        tvTotalReviews.setText(totalReviews + " " + reviewsText);
    }
    
    /**
     * Refresh reviews data
     */
    public void refreshReviews() {
        loadHelperReviews();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // Refresh reviews when returning to this activity
        // This helps show new reviews if user just created one
        if (reviewAdapter != null && !reviewAdapter.isEmpty()) {
            refreshReviews();
        }
    }
}
