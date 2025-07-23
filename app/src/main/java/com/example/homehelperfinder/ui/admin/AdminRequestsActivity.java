package com.example.homehelperfinder.ui.admin;

import android.animation.ObjectAnimator;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homehelperfinder.R;
import com.example.homehelperfinder.data.model.RequestItem;
import com.example.homehelperfinder.data.model.response.AdminRequestsResponse;
import com.example.homehelperfinder.data.remote.BaseApiService;
import com.example.homehelperfinder.data.remote.admin.AdminRequestsApiService;
import com.example.homehelperfinder.ui.admin.adapter.RequestAdapter;
import com.example.homehelperfinder.ui.base.BaseActivity;
import com.example.homehelperfinder.ui.base.BaseAdminActivity;
import com.example.homehelperfinder.utils.Logger;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Activity for managing admin requests with filtering and pagination
 */
public class AdminRequestsActivity extends BaseAdminActivity implements RequestAdapter.OnRequestClickListener {

    private static final String TAG = "AdminRequestsActivity";

    // UI Components
    private Toolbar toolbar;

    // Filter components
    private LinearLayout layoutFilterHeader;
    private ImageView ivExpandArrow;
    private LinearLayout layoutFilterContent;
    private Chip chipPending, chipApproved, chipRejected, chipToday;
    private AutoCompleteTextView spinnerStatus;
    private TextInputEditText etUserSearch;
    private TextInputEditText etDateFrom;
    private TextInputEditText etDateTo;
    private TextInputEditText etLocationSearch;
    private MaterialButton btnClearFilters;
    private MaterialButton btnApplyFilters;

    // Content components
    private RecyclerView rvRequests;
    private LinearLayout layoutEmptyState;
    private ProgressBar progressBar;
    private LinearLayout layoutPagination;
    private MaterialButton btnPrevious;
    private TextView tvPageInfo;
    private MaterialButton btnNext;

    // Filter state
    private boolean isFilterExpanded = false;

    // Data and API
    private AdminRequestsApiService apiService;
    private RequestAdapter adapter;
    private List<RequestItem> requestList;

    // Pagination and Filtering
    private int currentPage = 1;
    private int pageSize = 20;
    private int totalPages = 1;
    private String currentStatus = null;
    private String currentUser = null;
    private String currentDateFrom = null;
    private String currentDateTo = null;
    private String currentLocation = null;

    // Date formatter
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
    private final SimpleDateFormat displayDateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_requests);

        initViews();
        setupToolbar();
        setupRecyclerView();
        setupFilters();
        setupFilterToggle();
        setupQuickFilters();
        setupPagination();
        setupClickListeners();

        // Initialize API service
        apiService = new AdminRequestsApiService();

        // Load initial data
        loadRequests();
    }

    @Override
    protected AdminPage getCurrentPage() {
        return AdminPage.REQUESTS;
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);

        // Filter views
        layoutFilterHeader = findViewById(R.id.layout_filter_header);
        ivExpandArrow = findViewById(R.id.iv_expand_arrow);
        layoutFilterContent = findViewById(R.id.layout_filter_content);
        chipPending = findViewById(R.id.chip_pending);
        chipApproved = findViewById(R.id.chip_approved);
        chipRejected = findViewById(R.id.chip_rejected);
        chipToday = findViewById(R.id.chip_today);
        spinnerStatus = findViewById(R.id.spinner_status);
        etUserSearch = findViewById(R.id.et_user_search);
        etDateFrom = findViewById(R.id.et_date_from);
        etDateTo = findViewById(R.id.et_date_to);
        etLocationSearch = findViewById(R.id.et_location_search);
        btnClearFilters = findViewById(R.id.btn_clear_filters);
        btnApplyFilters = findViewById(R.id.btn_apply_filters);

        // Content views
        rvRequests = findViewById(R.id.rv_requests);
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
        }

        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void setupRecyclerView() {
        requestList = new ArrayList<>();
        adapter = new RequestAdapter(this, requestList);
        adapter.setOnRequestClickListener(this);

        rvRequests.setLayoutManager(new LinearLayoutManager(this));
        rvRequests.setAdapter(adapter);
    }

    private void setupFilters() {
        // Setup status spinner
        String[] statusOptions = {"Tất cả", "Pending", "Confirmed", "In Progress", "Completed", "Cancelled", "Rejected"};
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, statusOptions);
        spinnerStatus.setAdapter(statusAdapter);
        spinnerStatus.setText("Tất cả", false);

        // Setup date pickers
        etDateFrom.setOnClickListener(v -> showDatePicker(etDateFrom, true));
        etDateTo.setOnClickListener(v -> showDatePicker(etDateTo, false));

        // Setup search text watchers with debounce
        setupSearchTextWatcher(etUserSearch);
        setupSearchTextWatcher(etLocationSearch);
    }

    private void setupSearchTextWatcher(TextInputEditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Could implement debounce here for auto-search
            }
        });
    }

    private void setupPagination() {
        updatePaginationUI();
    }

    private void setupFilterToggle() {
        layoutFilterHeader.setOnClickListener(v -> toggleFilterVisibility());
    }

    private void setupQuickFilters() {
        chipPending.setOnClickListener(v -> applyQuickFilter("pending"));
        chipApproved.setOnClickListener(v -> applyQuickFilter("confirmed"));
        chipRejected.setOnClickListener(v -> applyQuickFilter("rejected"));
        chipToday.setOnClickListener(v -> applyTodayFilter());
    }

    private void toggleFilterVisibility() {
        isFilterExpanded = !isFilterExpanded;

        if (isFilterExpanded) {
            layoutFilterContent.setVisibility(View.VISIBLE);
            rotateArrow(0f, 180f);
        } else {
            layoutFilterContent.setVisibility(View.GONE);
            rotateArrow(180f, 0f);
        }
    }

    private void rotateArrow(float from, float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(ivExpandArrow, "rotation", from, to);
        animator.setDuration(200);
        animator.start();
    }

    private void applyQuickFilter(String status) {
        // Clear other quick filters
        clearQuickFilters();

        // Set the selected status
        currentStatus = status;

        // Update spinner to reflect the selection
        String displayStatus = getDisplayStatus(status);
        spinnerStatus.setText(displayStatus, false);

        // Reset to first page and load
        currentPage = 1;
        loadRequests();
    }

    private void applyTodayFilter() {
        // Clear other quick filters
        clearQuickFilters();

        // Set today's date
        Calendar today = Calendar.getInstance();
        String todayStr = displayDateFormatter.format(today.getTime());

        etDateFrom.setText(todayStr);
        etDateTo.setText(todayStr);

        currentDateFrom = convertDisplayDateToApiFormat(todayStr);
        currentDateTo = convertDisplayDateToApiFormat(todayStr);

        // Reset to first page and load
        currentPage = 1;
        loadRequests();
    }

    private void clearQuickFilters() {
        chipPending.setChecked(false);
        chipApproved.setChecked(false);
        chipRejected.setChecked(false);
        chipToday.setChecked(false);
    }

    private String getDisplayStatus(String status) {
        switch (status.toLowerCase()) {
            case "pending":
                return "Pending";
            case "confirmed":
                return "Confirmed";
            case "rejected":
                return "Rejected";
            default:
                return "Tất cả";
        }
    }

    private void setupClickListeners() {
        btnApplyFilters.setOnClickListener(v -> applyFilters());
        btnClearFilters.setOnClickListener(v -> clearFilters());
        btnPrevious.setOnClickListener(v -> goToPreviousPage());
        btnNext.setOnClickListener(v -> goToNextPage());
    }

    private void showDatePicker(TextInputEditText editText, boolean isFromDate) {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    String formattedDate = displayDateFormatter.format(calendar.getTime());
                    editText.setText(formattedDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }

    private void applyFilters() {
        // Get filter values
        String selectedStatus = spinnerStatus.getText().toString();
        currentStatus = selectedStatus.equals("Tất cả") ? null : selectedStatus.toLowerCase();
        currentUser = etUserSearch.getText().toString().trim();
        currentLocation = etLocationSearch.getText().toString().trim();

        // Convert display dates to API format
        currentDateFrom = convertDisplayDateToApiFormat(etDateFrom.getText().toString());
        currentDateTo = convertDisplayDateToApiFormat(etDateTo.getText().toString());

        // Reset to first page and load
        currentPage = 1;
        loadRequests();
    }

    private void clearFilters() {
        // Clear quick filters
        clearQuickFilters();

        // Clear form fields
        spinnerStatus.setText("Tất cả", false);
        etUserSearch.setText("");
        etDateFrom.setText("");
        etDateTo.setText("");
        etLocationSearch.setText("");

        // Clear filter values
        currentStatus = null;
        currentUser = null;
        currentDateFrom = null;
        currentDateTo = null;
        currentLocation = null;

        // Reset to first page and load
        currentPage = 1;
        loadRequests();
    }

    private String convertDisplayDateToApiFormat(String displayDate) {
        if (displayDate == null || displayDate.trim().isEmpty()) {
            return null;
        }

        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(displayDateFormatter.parse(displayDate));
            return dateFormatter.format(calendar.getTime());
        } catch (Exception e) {
            Logger.e(TAG, "Error converting date: " + displayDate, e);
            return null;
        }
    }

    private void goToPreviousPage() {
        if (currentPage > 1) {
            currentPage--;
            loadRequests();
        }
    }

    private void goToNextPage() {
        if (currentPage < totalPages) {
            currentPage++;
            loadRequests();
        }
    }

    private void refreshData() {
        currentPage = 1;
        loadRequests();
    }

    private void loadRequests() {
        showLoading(true);

        // Prepare filter values (empty strings should be null)
        String status = (currentStatus != null && !currentStatus.trim().isEmpty()) ? currentStatus : null;
        String user = (currentUser != null && !currentUser.trim().isEmpty()) ? currentUser : null;
        String location = (currentLocation != null && !currentLocation.trim().isEmpty()) ? currentLocation : null;

        Logger.d(TAG, String.format("Loading requests - page: %d, status: %s, user: %s, location: %s",
                currentPage, status, user, location));

        apiService.getAdminRequests(this, status, user, currentDateFrom, currentDateTo,
                location, currentPage, pageSize, new BaseApiService.ApiCallback<AdminRequestsResponse>() {
                    @Override
                    public void onSuccess(AdminRequestsResponse response) {
                        runOnUiThread(() -> {
                            showLoading(false);
                            handleRequestsResponse(response);
                        });
                    }

                    @Override
                    public void onError(String errorMessage, Throwable throwable) {
                        runOnUiThread(() -> {
                            showLoading(false);
                            showLongToast("Lỗi khi tải danh sách requests: " + errorMessage);
                            showEmptyState(true);
                            Logger.e(TAG, "Error loading requests: " + errorMessage, throwable);
                        });
                    }
                });
    }

    private void handleRequestsResponse(AdminRequestsResponse response) {
        if (response != null && response.getRequests() != null) {
            adapter.updateRequests(response.getRequests());

            // Update pagination info
            totalPages = response.getTotalPages();
            updatePaginationUI();

            // Show/hide empty state
            showEmptyState(response.getRequests().isEmpty());

            Logger.i(TAG, String.format("Loaded %d requests, page %d of %d",
                    response.getRequests().size(), currentPage, totalPages));
        } else {
            showEmptyState(true);
        }
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        rvRequests.setVisibility(show ? View.GONE : View.VISIBLE);
        layoutPagination.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private void showEmptyState(boolean show) {
        layoutEmptyState.setVisibility(show ? View.VISIBLE : View.GONE);
        rvRequests.setVisibility(show ? View.GONE : View.VISIBLE);
        layoutPagination.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private void updatePaginationUI() {
        tvPageInfo.setText(String.format(Locale.getDefault(), "Trang %d / %d", currentPage, totalPages));
        btnPrevious.setEnabled(currentPage > 1);
        btnNext.setEnabled(currentPage < totalPages);
    }

    @Override
    public void onRequestClick(RequestItem request, int position) {
        // TODO: Navigate to request detail activity
        showToast("Clicked request #" + request.getRequestId());
        Logger.d(TAG, "Request clicked: " + request.getRequestId());
    }
}
