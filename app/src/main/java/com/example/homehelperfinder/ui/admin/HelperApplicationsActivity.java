package com.example.homehelperfinder.ui.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homehelperfinder.R;
import com.example.homehelperfinder.data.model.HelperApplication;
import com.example.homehelperfinder.data.model.response.HelperApplicationsResponse;
import com.example.homehelperfinder.data.remote.BaseApiService;
import com.example.homehelperfinder.data.remote.admin.HelperApplicationsApiService;
import com.example.homehelperfinder.ui.base.BaseActivity;
import com.example.homehelperfinder.ui.base.BaseAdminActivity;
import com.example.homehelperfinder.utils.Logger;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Activity for managing helper applications
 */
public class HelperApplicationsActivity extends BaseAdminActivity implements HelperApplicationAdapter.OnApplicationClickListener {

    private static final String TAG = "HelperApplicationsActivity";

    // UI Components
    private Toolbar toolbar;
    private AutoCompleteTextView spinnerStatus;
    private RecyclerView rvApplications;
    private LinearLayout layoutEmptyState;
    private ProgressBar progressBar;
    private LinearLayout layoutPagination;
    private MaterialButton btnPrevious;
    private TextView tvPageInfo;
    private MaterialButton btnNext;

    // Data
    private HelperApplicationsApiService apiService;
    private HelperApplicationAdapter adapter;
    private List<HelperApplication> applicationList;

    // Pagination
    private int currentPage = 1;
    private int pageSize = 20;
    private int totalPages = 1;

    // Filters
    private String currentStatus = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helper_applications);

        initViews();
        setupToolbar();
        setupRecyclerView();
        setupFilters();
        setupPagination();

        apiService = new HelperApplicationsApiService();

        // Load initial data
        loadApplications();
    }

    @Override
    protected AdminPage getCurrentPage() {
        return AdminPage.APPLICATIONS;
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        spinnerStatus = findViewById(R.id.spinner_status);
        rvApplications = findViewById(R.id.rv_applications);
        layoutEmptyState = findViewById(R.id.layout_empty_state);
        progressBar = findViewById(R.id.progress_bar);
        layoutPagination = findViewById(R.id.layout_pagination);
        btnPrevious = findViewById(R.id.btn_previous);
        tvPageInfo = findViewById(R.id.tv_page_info);
        btnNext = findViewById(R.id.btn_next);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Helper Applications");
        }

        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void setupRecyclerView() {
        applicationList = new ArrayList<>();
        adapter = new HelperApplicationAdapter(this, applicationList);
        adapter.setOnApplicationClickListener(this);

        rvApplications.setLayoutManager(new LinearLayoutManager(this));
        rvApplications.setAdapter(adapter);
    }

    private void setupFilters() {
        // Setup status filter dropdown
        String[] statusOptions = {"All", "Pending", "Approved", "Rejected", "Revision Requested"};
        // You can use ArrayAdapter here for the dropdown
        // ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, statusOptions);
        // spinnerStatus.setAdapter(statusAdapter);
    }

    private void setupPagination() {
        btnPrevious.setOnClickListener(v -> {
            if (currentPage > 1) {
                currentPage--;
                loadApplications();
            }
        });

        btnNext.setOnClickListener(v -> {
            if (currentPage < totalPages) {
                currentPage++;
                loadApplications();
            }
        });
    }

    private void applyFilters() {
        String selectedStatus = spinnerStatus.getText().toString().trim();

        // Convert display text to API status
        switch (selectedStatus) {
            case "All":
                currentStatus = null;
                break;
            case "Pending":
                currentStatus = "pending";
                break;
            case "Approved":
                currentStatus = "approved";
                break;
            case "Rejected":
                currentStatus = "rejected";
                break;
            case "Revision Requested":
                currentStatus = "revision_requested";
                break;
            default:
                currentStatus = null;
                break;
        }

        currentPage = 1;
        loadApplications();
    }

    private void clearFilters() {
        spinnerStatus.setText("All", false);
        currentStatus = null;
        currentPage = 1;
        loadApplications();
    }

    private void loadApplications() {
        showLoading(true);

        Logger.d(TAG, String.format("Loading applications - page: %d, status: %s", currentPage, currentStatus));

        apiService.getHelperApplications(this, currentStatus, currentPage, pageSize,
                new BaseApiService.ApiCallback<HelperApplicationsResponse>() {
                    @Override
                    public void onSuccess(HelperApplicationsResponse response) {
                        runOnUiThread(() -> {
                            showLoading(false);
                            handleApplicationsResponse(response);
                        });
                    }

                    @Override
                    public void onError(String errorMessage, Throwable throwable) {
                        runOnUiThread(() -> {
                            showLoading(false);
                            showLongToast("Error loading applications: " + errorMessage);
                            showEmptyState(true);
                            Logger.e(TAG, "Error loading applications: " + errorMessage, throwable);
                        });
                    }
                });
    }

    private void handleApplicationsResponse(HelperApplicationsResponse response) {
        if (response != null && response.hasApplications()) {
            adapter.updateApplications(response.getApplications());

            // Update pagination info
            if (response.getPagination() != null) {
                totalPages = response.getTotalPages();
                updatePaginationUI();
            }

            // Show/hide empty state
            showEmptyState(false);

            Logger.i(TAG, String.format("Loaded %d applications, page %d of %d",
                    response.getApplicationCount(), currentPage, totalPages));
        } else {
            showEmptyState(true);
        }
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        rvApplications.setVisibility(show ? View.GONE : View.VISIBLE);
        layoutPagination.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private void showEmptyState(boolean show) {
        layoutEmptyState.setVisibility(show ? View.VISIBLE : View.GONE);
        rvApplications.setVisibility(show ? View.GONE : View.VISIBLE);
        layoutPagination.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private void updatePaginationUI() {
        tvPageInfo.setText(String.format(Locale.getDefault(), "Page %d / %d", currentPage, totalPages));
        btnPrevious.setEnabled(currentPage > 1);
        btnNext.setEnabled(currentPage < totalPages);
    }

    @Override
    public void onApplicationClick(HelperApplication application, int position) {
        // Navigate to application detail
        Intent intent = new Intent(this, HelperApplicationDetailActivity.class);
        intent.putExtra("helperId", application.getHelperId());
        intent.putExtra("helperName", application.getFullName());
        startActivity(intent);

        Logger.d(TAG, "Application clicked: " + application.getHelperId());
    }
}
