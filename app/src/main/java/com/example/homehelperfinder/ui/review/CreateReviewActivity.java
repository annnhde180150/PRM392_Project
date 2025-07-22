package com.example.homehelperfinder.ui.review;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.homehelperfinder.R;
import com.example.homehelperfinder.data.model.response.ReviewResponse;
import com.example.homehelperfinder.data.repository.ReviewRepository;
import com.example.homehelperfinder.data.remote.BaseApiService;
import com.example.homehelperfinder.ui.base.BaseActivity;
import com.example.homehelperfinder.utils.Logger;

/**
 * Activity for creating a new review
 */
public class CreateReviewActivity extends BaseActivity {
    
    public static final String EXTRA_BOOKING_ID = "booking_id";
    public static final String EXTRA_HELPER_ID = "helper_id";
    public static final String EXTRA_HELPER_NAME = "helper_name";
    public static final String EXTRA_SERVICE_NAME = "service_name";
    
    private RatingBar rbRating;
    private EditText etComment;
    private TextView tvCharacterCount;
    private TextView tvHelperName;
    private TextView tvServiceName;
    private Button btnSubmitReview;
    
    private ReviewRepository reviewRepository;
    
    private int bookingId;
    private int helperId;
    private String helperName;
    private String serviceName;
    
    private static final int MAX_COMMENT_LENGTH = 500;
    private static final int MIN_COMMENT_LENGTH = 10;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_review);
        
        // Get data from intent
        Intent intent = getIntent();
        bookingId = intent.getIntExtra(EXTRA_BOOKING_ID, -1);
        helperId = intent.getIntExtra(EXTRA_HELPER_ID, -1);
        helperName = intent.getStringExtra(EXTRA_HELPER_NAME);
        serviceName = intent.getStringExtra(EXTRA_SERVICE_NAME);
        
        if (bookingId == -1 || helperId == -1) {
            showToast("Invalid booking or helper information");
            finish();
            return;
        }
        
        initViews();
        setupListeners();
        setupMenuNavigation();
        
        reviewRepository = new ReviewRepository();
    }
    
    private void initViews() {
        rbRating = findViewById(R.id.rb_rating);
        etComment = findViewById(R.id.et_comment);
        tvCharacterCount = findViewById(R.id.tv_character_count);
        tvHelperName = findViewById(R.id.tv_helper_name);
        tvServiceName = findViewById(R.id.tv_service_name);
        btnSubmitReview = findViewById(R.id.btn_submit_review);
        
        // Set helper and service information
        if (helperName != null && !helperName.isEmpty()) {
            tvHelperName.setText(helperName);
        } else {
            tvHelperName.setText("Helper");
        }
        
        if (serviceName != null && !serviceName.isEmpty()) {
            tvServiceName.setText(serviceName);
        } else {
            tvServiceName.setText("Service");
        }
        
        // Setup back button
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
        
        // Initialize character count
        updateCharacterCount();
        
        // Initially disable submit button
        updateSubmitButtonState();
    }
    
    private void setupListeners() {
        // Rating change listener
        rbRating.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            if (fromUser) {
                updateSubmitButtonState();
            }
        });
        
        // Comment text change listener
        etComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateCharacterCount();
                updateSubmitButtonState();
            }
            
            @Override
            public void afterTextChanged(Editable s) {}
        });
        
        // Submit button click listener
        btnSubmitReview.setOnClickListener(v -> submitReview());
    }
    
    private void updateCharacterCount() {
        int currentLength = etComment.getText().toString().trim().length();
        String countText = currentLength + "/" + MAX_COMMENT_LENGTH;
        tvCharacterCount.setText(countText);
        
        // Change color based on length
        if (currentLength > MAX_COMMENT_LENGTH) {
            tvCharacterCount.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        } else if (currentLength < MIN_COMMENT_LENGTH) {
            tvCharacterCount.setTextColor(getResources().getColor(R.color.text_secondary));
        } else {
            tvCharacterCount.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        }
    }
    
    private void updateSubmitButtonState() {
        boolean isValid = isReviewValid();
        btnSubmitReview.setEnabled(isValid);
        btnSubmitReview.setAlpha(isValid ? 1.0f : 0.5f);
    }
    
    private boolean isReviewValid() {
        float rating = rbRating.getRating();
        String comment = etComment.getText().toString().trim();
        
        return rating > 0 && 
               comment.length() >= MIN_COMMENT_LENGTH && 
               comment.length() <= MAX_COMMENT_LENGTH;
    }
    
    private void submitReview() {
        if (!isReviewValid()) {
            showToast("Please provide a valid rating and comment");
            return;
        }
        
        int rating = (int) rbRating.getRating();
        String comment = etComment.getText().toString().trim();
        
        showProgressDialog("Submitting review...");
        
        reviewRepository.createReview(this, bookingId, helperId, rating, comment, 
            new BaseApiService.ApiCallback<ReviewResponse>() {
                @Override
                public void onSuccess(ReviewResponse review) {
                    hideProgressDialog();
                    Logger.i("CreateReviewActivity", "Review created successfully: " + review.getReviewId());
                    showToast("Review submitted successfully!");
                    
                    // Set result and finish
                    setResult(RESULT_OK);
                    finish();
                }
                
                @Override
                public void onError(String errorMessage, Throwable throwable) {
                    hideProgressDialog();
                    Logger.e("CreateReviewActivity", "Error creating review: " + errorMessage, throwable);
                    showToast("Failed to submit review: " + errorMessage);
                }
            });
    }
    
    @Override
    public void onBackPressed() {
        // Check if user has entered any data
        if (rbRating.getRating() > 0 || !etComment.getText().toString().trim().isEmpty()) {
            // Show confirmation dialog using AlertDialog
            new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Discard Review?")
                .setMessage("Are you sure you want to discard your review?")
                .setPositiveButton("Discard", (dialog, which) -> {
                    super.onBackPressed();
                })
                .setNegativeButton("Continue", null)
                .show();
        } else {
            super.onBackPressed();
        }
    }
}
