package com.example.homehelperfinder.ui.admin;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.homehelperfinder.R;
import com.example.homehelperfinder.data.model.HelperApplicationDetail;
import com.example.homehelperfinder.data.model.request.ApplicationDecisionRequest;
import com.example.homehelperfinder.data.model.response.ApplicationDecisionResponse;
import com.example.homehelperfinder.data.remote.BaseApiService;
import com.example.homehelperfinder.data.remote.admin.HelperApplicationsApiService;
import com.example.homehelperfinder.ui.base.BaseActivity;
import com.example.homehelperfinder.utils.Logger;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;

/**
 * Activity for viewing detailed helper application information
 */
public class HelperApplicationDetailActivity extends BaseActivity {
    
    private static final String TAG = "HelperApplicationDetailActivity";
    
    // UI Components
    private Toolbar toolbar;
    private ProgressBar progressBar;
    private ScrollView layoutContent;
    private LinearLayout layoutEmptyState;
    
    // Content Views
    private TextView tvHelperName;
    private TextView tvEmail;
    private TextView tvPhoneNumber;
    private TextView tvBio;
    private TextView tvDateOfBirth;
    private TextView tvGender;
    private TextView tvRegistrationDate;
    private Chip chipStatus;
    private TextView tvDocumentStats;
    private TextView tvSkillCount;
    private TextView tvWorkAreaCount;
    
    // Action Buttons
    private MaterialButton btnApprove;
    private MaterialButton btnReject;
    private MaterialButton btnRequestRevision;
    
    // Data
    private HelperApplicationsApiService apiService;
    private int helperId;
    private String helperName;
    private HelperApplicationDetail applicationDetail;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helper_application_detail);
        
        // Get data from intent
        Intent intent = getIntent();
        helperId = intent.getIntExtra("helperId", -1);
        helperName = intent.getStringExtra("helperName");
        
        if (helperId == -1) {
            showToast("Invalid helper ID");
            finish();
            return;
        }
        
        initViews();
        setupToolbar();
        setupClickListeners();
        
        apiService = new HelperApplicationsApiService();
        
        // Load application detail
        loadApplicationDetail();
    }
    
    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        progressBar = findViewById(R.id.progress_bar);
        layoutContent = findViewById(R.id.layout_content);
        layoutEmptyState = findViewById(R.id.layout_empty_state);
        
        // Content views
        tvHelperName = findViewById(R.id.tv_helper_name);
        tvEmail = findViewById(R.id.tv_email);
        tvPhoneNumber = findViewById(R.id.tv_phone_number);
        tvBio = findViewById(R.id.tv_bio);
        tvDateOfBirth = findViewById(R.id.tv_date_of_birth);
        tvGender = findViewById(R.id.tv_gender);
        tvRegistrationDate = findViewById(R.id.tv_registration_date);
        chipStatus = findViewById(R.id.chip_status);
        tvDocumentStats = findViewById(R.id.tv_document_stats);
        tvSkillCount = findViewById(R.id.tv_skill_count);
        tvWorkAreaCount = findViewById(R.id.tv_work_area_count);
        
        // Action buttons
        btnApprove = findViewById(R.id.btn_approve);
        btnReject = findViewById(R.id.btn_reject);
        btnRequestRevision = findViewById(R.id.btn_request_revision);
    }
    
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(helperName != null ? helperName : "Helper Application");
        }
        
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }
    
    private void setupClickListeners() {
        btnApprove.setOnClickListener(v -> approveApplication());
        btnReject.setOnClickListener(v -> rejectApplication());
        btnRequestRevision.setOnClickListener(v -> requestRevision());
    }
    
    private void loadApplicationDetail() {
        showLoading(true);
        
        Logger.d(TAG, "Loading application detail for helper ID: " + helperId);
        
        apiService.getHelperApplicationDetail(this, helperId, new BaseApiService.ApiCallback<HelperApplicationDetail>() {
            @Override
            public void onSuccess(HelperApplicationDetail detail) {
                runOnUiThread(() -> {
                    showLoading(false);
                    applicationDetail = detail;
                    displayApplicationDetail(detail);
                });
            }

            @Override
            public void onError(String errorMessage, Throwable throwable) {
                runOnUiThread(() -> {
                    showLoading(false);
                    showLongToast("Error loading application detail: " + errorMessage);
                    showEmptyState(true);
                    Logger.e(TAG, "Error loading application detail: " + errorMessage, throwable);
                });
            }
        });
    }
    
    private void displayApplicationDetail(HelperApplicationDetail detail) {
        // Basic info
        tvHelperName.setText(detail.getFullName());
        tvEmail.setText(detail.getEmail());
        tvPhoneNumber.setText(detail.getPhoneNumber());
        tvBio.setText(detail.hasBio() ? detail.getBio() : "No bio provided");
        tvDateOfBirth.setText(detail.getDateOfBirth());
        tvGender.setText(detail.getGender());
        tvRegistrationDate.setText(detail.getRegistrationDate());
        
        // Status
        setupStatusChip(detail);
        
        // Stats
        String docStats = String.format("%d/%d verified (%d pending)", 
                detail.getVerifiedDocuments(), 
                detail.getTotalDocuments(), 
                detail.getPendingDocuments());
        tvDocumentStats.setText(docStats);
        
        tvSkillCount.setText(String.valueOf(detail.getSkills() != null ? detail.getSkills().size() : 0));
        tvWorkAreaCount.setText(String.valueOf(detail.getWorkAreas() != null ? detail.getWorkAreas().size() : 0));
        
        // Show/hide action buttons based on status
        updateActionButtons(detail);
        
        showContent(true);
    }
    
    private void setupStatusChip(HelperApplicationDetail detail) {
        chipStatus.setText(detail.getFormattedApprovalStatus());

        // Set chip colors based on status
        int chipBackgroundColor;
        int chipTextColor;

        if (detail.isPending()) {
            chipBackgroundColor = ContextCompat.getColor(this, R.color.status_pending_bg);
            chipTextColor = ContextCompat.getColor(this, R.color.status_pending_text);
        } else if (detail.isApproved()) {
            chipBackgroundColor = ContextCompat.getColor(this, R.color.status_approved_bg);
            chipTextColor = ContextCompat.getColor(this, R.color.status_approved_text);
        } else if (detail.isRejected()) {
            chipBackgroundColor = ContextCompat.getColor(this, R.color.status_rejected_bg);
            chipTextColor = ContextCompat.getColor(this, R.color.status_rejected_text);
        } else if (detail.isRevisionRequested()) {
            chipBackgroundColor = ContextCompat.getColor(this, R.color.status_revision_bg);
            chipTextColor = ContextCompat.getColor(this, R.color.status_revision_text);
        } else {
            chipBackgroundColor = ContextCompat.getColor(this, R.color.status_unknown_bg);
            chipTextColor = ContextCompat.getColor(this, R.color.status_unknown_text);
        }

        chipStatus.setChipBackgroundColor(ColorStateList.valueOf(chipBackgroundColor));
        chipStatus.setTextColor(chipTextColor);
    }

    private void updateActionButtons(HelperApplicationDetail detail) {
        boolean isPending = detail.isPending();
        btnApprove.setVisibility(isPending ? View.VISIBLE : View.GONE);
        btnReject.setVisibility(isPending ? View.VISIBLE : View.GONE);
        btnRequestRevision.setVisibility(isPending ? View.VISIBLE : View.GONE);
    }
    
    private void approveApplication() {
        makeDecision("approved", "Application approved");
    }
    
    private void rejectApplication() {
        makeDecision("rejected", "Application rejected - please review requirements");
    }
    
    private void requestRevision() {
        makeDecision("revision_requested", "Please update your application with additional information");
    }
    
    private void makeDecision(String status, String comment) {
        showProgressDialog("Processing decision...");
        
        ApplicationDecisionRequest request = new ApplicationDecisionRequest(status, comment);
        
        apiService.makeApplicationDecision(this, helperId, request, new BaseApiService.ApiCallback<ApplicationDecisionResponse>() {
            @Override
            public void onSuccess(ApplicationDecisionResponse response) {
                runOnUiThread(() -> {
                    hideProgressDialog();
                    showToast("Decision processed successfully");
                    // Reload the application detail to show updated status
                    loadApplicationDetail();
                });
            }

            @Override
            public void onError(String errorMessage, Throwable throwable) {
                runOnUiThread(() -> {
                    hideProgressDialog();
                    showLongToast("Error processing decision: " + errorMessage);
                    Logger.e(TAG, "Error processing decision: " + errorMessage, throwable);
                });
            }
        });
    }
    
    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        layoutContent.setVisibility(show ? View.GONE : View.VISIBLE);
    }
    
    private void showContent(boolean show) {
        layoutContent.setVisibility(show ? View.VISIBLE : View.GONE);
        layoutEmptyState.setVisibility(show ? View.GONE : View.VISIBLE);
    }
    
    private void showEmptyState(boolean show) {
        layoutEmptyState.setVisibility(show ? View.VISIBLE : View.GONE);
        layoutContent.setVisibility(show ? View.GONE : View.VISIBLE);
    }
}
