package com.example.homehelperfinder.ui.admin;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.homehelperfinder.R;
import com.example.homehelperfinder.data.model.response.BookingAnalyticsResponse;
import com.example.homehelperfinder.data.model.response.BusinessOverviewResponse;
import com.example.homehelperfinder.data.model.response.HelperRankingResponse;
import com.example.homehelperfinder.data.model.response.RevenueAnalyticsResponse;
import com.example.homehelperfinder.data.model.response.ServicePerformanceResponse;
import com.example.homehelperfinder.data.remote.BaseApiService;
import com.example.homehelperfinder.data.remote.admin.AdminReportApiService;
import com.example.homehelperfinder.ui.base.BaseActivity;
import com.example.homehelperfinder.ui.base.BaseAdminActivity;
import com.example.homehelperfinder.ui.admin.adapter.HelperRankingAdapter;
import com.example.homehelperfinder.ui.admin.adapter.ServicePerformanceAdapter;
import com.example.homehelperfinder.utils.Logger;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Admin Analytics Dashboard Activity
 * Displays comprehensive analytics including business overview, revenue, service performance, and helper rankings
 */
public class AdminAnalyticsActivity extends BaseAdminActivity {

    private static final String TAG = "AdminAnalyticsActivity";

    // UI Components
    private Toolbar toolbar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;
    private AutoCompleteTextView spinnerPeriod;

    // Overview Cards
    private CardView cardTotalUsers, cardTotalHelpers, cardTotalBookings, cardTotalRevenue;
    private TextView tvTotalUsers, tvTotalUsersGrowth;
    private TextView tvTotalHelpers, tvTotalHelpersGrowth;
    private TextView tvTotalBookings, tvTotalBookingsGrowth;
    private TextView tvTotalRevenue, tvTotalRevenueGrowth;

    // Revenue Analytics
    private CardView cardRevenueAnalytics;
    private TextView tvNetRevenue, tvPlatformFees, tvHelperEarnings, tvPaymentSuccessRate;

    // Service Performance
    private RecyclerView rvServicePerformance;
    private ServicePerformanceAdapter servicePerformanceAdapter;
    private TextView tvServicePerformanceEmpty;

    // Helper Rankings
    private RecyclerView rvHelperRankings;
    private HelperRankingAdapter helperRankingAdapter;
    private TextView tvHelperRankingsEmpty;

    // Booking Analytics
    private CardView cardBookingAnalytics;
    private TextView tvPendingBookings, tvCompletedBookings, tvCancelledBookings;
    private TextView tvCompletionRate, tvCancellationRate;

    // Data and API
    private AdminReportApiService apiService;
    private String currentPeriod = "month";
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
    private final NumberFormat percentFormat = NumberFormat.getPercentInstance(Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_analytics);

        initViews();
        setupToolbar();
        setupPeriodFilter();
        setupRecyclerViews();
        setupSwipeRefresh();

        // Initialize API service
        apiService = new AdminReportApiService();

        // Load initial data
        loadAllAnalytics();
    }

    @Override
    protected AdminPage getCurrentPage() {
        return AdminPage.ANALYTICS;
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        progressBar = findViewById(R.id.progress_bar);
        spinnerPeriod = findViewById(R.id.spinner_period);

        // Overview Cards
        cardTotalUsers = findViewById(R.id.card_total_users);
        cardTotalHelpers = findViewById(R.id.card_total_helpers);
        cardTotalBookings = findViewById(R.id.card_total_bookings);
        cardTotalRevenue = findViewById(R.id.card_total_revenue);

        tvTotalUsers = findViewById(R.id.tv_total_users);
        tvTotalUsersGrowth = findViewById(R.id.tv_total_users_growth);
        tvTotalHelpers = findViewById(R.id.tv_total_helpers);
        tvTotalHelpersGrowth = findViewById(R.id.tv_total_helpers_growth);
        tvTotalBookings = findViewById(R.id.tv_total_bookings);
        tvTotalBookingsGrowth = findViewById(R.id.tv_total_bookings_growth);
        tvTotalRevenue = findViewById(R.id.tv_total_revenue);
        tvTotalRevenueGrowth = findViewById(R.id.tv_total_revenue_growth);

        // Revenue Analytics
        cardRevenueAnalytics = findViewById(R.id.card_revenue_analytics);
        tvNetRevenue = findViewById(R.id.tv_net_revenue);
        tvPlatformFees = findViewById(R.id.tv_platform_fees);
        tvHelperEarnings = findViewById(R.id.tv_helper_earnings);
        tvPaymentSuccessRate = findViewById(R.id.tv_payment_success_rate);

        // Service Performance
        rvServicePerformance = findViewById(R.id.rv_service_performance);
        tvServicePerformanceEmpty = findViewById(R.id.tv_service_performance_empty);

        // Helper Rankings
        rvHelperRankings = findViewById(R.id.rv_helper_rankings);
        tvHelperRankingsEmpty = findViewById(R.id.tv_helper_rankings_empty);

        // Booking Analytics
        cardBookingAnalytics = findViewById(R.id.card_booking_analytics);
        tvPendingBookings = findViewById(R.id.tv_pending_bookings);
        tvCompletedBookings = findViewById(R.id.tv_completed_bookings);
        tvCancelledBookings = findViewById(R.id.tv_cancelled_bookings);
        tvCompletionRate = findViewById(R.id.tv_completion_rate);
        tvCancellationRate = findViewById(R.id.tv_cancellation_rate);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Analytics Dashboard");
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void setupPeriodFilter() {
        String[] periods = {"month", "week", "year"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, 
                android.R.layout.simple_dropdown_item_1line, periods);
        spinnerPeriod.setAdapter(adapter);
        spinnerPeriod.setText("month", false);
        
        spinnerPeriod.setOnItemClickListener((parent, view, position, id) -> {
            currentPeriod = periods[position];
            loadAllAnalytics();
        });
    }

    private void setupRecyclerViews() {
        // Service Performance RecyclerView
        servicePerformanceAdapter = new ServicePerformanceAdapter(new ArrayList<>());
        rvServicePerformance.setLayoutManager(new LinearLayoutManager(this));
        rvServicePerformance.setAdapter(servicePerformanceAdapter);

        // Helper Rankings RecyclerView
        helperRankingAdapter = new HelperRankingAdapter(new ArrayList<>());
        rvHelperRankings.setLayoutManager(new LinearLayoutManager(this));
        rvHelperRankings.setAdapter(helperRankingAdapter);
    }

    private void setupSwipeRefresh() {
        swipeRefreshLayout.setOnRefreshListener(this::loadAllAnalytics);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.primary_color,
                R.color.secondary_color,
                R.color.accent_color
        );
    }

    private void loadAllAnalytics() {
        showProgressIndicator(true);
        loadBusinessOverview();
    }

    private void loadBusinessOverview() {
        apiService.getBusinessOverview(this, new BaseApiService.ApiCallback<BusinessOverviewResponse>() {
            @Override
            public void onSuccess(BusinessOverviewResponse response) {
                runOnUiThread(() -> {
                    updateBusinessOverview(response);
                    loadRevenueAnalytics();
                });
            }

            @Override
            public void onError(String errorMessage, Throwable throwable) {
                runOnUiThread(() -> {
                    showLongToast("Error loading business overview: " + errorMessage);
                    Logger.e(TAG, "Error loading business overview", throwable);
                    loadRevenueAnalytics(); // Continue loading other data
                });
            }
        });
    }

    private void loadRevenueAnalytics() {
        apiService.getRevenueAnalytics(this, currentPeriod, new BaseApiService.ApiCallback<RevenueAnalyticsResponse>() {
            @Override
            public void onSuccess(RevenueAnalyticsResponse response) {
                runOnUiThread(() -> {
                    updateRevenueAnalytics(response);
                    loadServicePerformance();
                });
            }

            @Override
            public void onError(String errorMessage, Throwable throwable) {
                runOnUiThread(() -> {
                    showLongToast("Error loading revenue analytics: " + errorMessage);
                    Logger.e(TAG, "Error loading revenue analytics", throwable);
                    loadServicePerformance(); // Continue loading other data
                });
            }
        });
    }

    private void loadServicePerformance() {
        apiService.getServicePerformance(this, currentPeriod, new BaseApiService.ApiCallback<List<ServicePerformanceResponse>>() {
            @Override
            public void onSuccess(List<ServicePerformanceResponse> response) {
                runOnUiThread(() -> {
                    updateServicePerformance(response);
                    loadHelperRankings();
                });
            }

            @Override
            public void onError(String errorMessage, Throwable throwable) {
                runOnUiThread(() -> {
                    showLongToast("Error loading service performance: " + errorMessage);
                    Logger.e(TAG, "Error loading service performance", throwable);
                    loadHelperRankings(); // Continue loading other data
                });
            }
        });
    }

    private void loadHelperRankings() {
        apiService.getHelperRankings(this, 10, currentPeriod, new BaseApiService.ApiCallback<List<HelperRankingResponse>>() {
            @Override
            public void onSuccess(List<HelperRankingResponse> response) {
                runOnUiThread(() -> {
                    updateHelperRankings(response);
                    loadBookingAnalytics();
                });
            }

            @Override
            public void onError(String errorMessage, Throwable throwable) {
                runOnUiThread(() -> {
                    showLongToast("Error loading helper rankings: " + errorMessage);
                    Logger.e(TAG, "Error loading helper rankings", throwable);
                    loadBookingAnalytics(); // Continue loading other data
                });
            }
        });
    }

    private void loadBookingAnalytics() {
        apiService.getBookingAnalytics(this, null, currentPeriod, new BaseApiService.ApiCallback<BookingAnalyticsResponse>() {
            @Override
            public void onSuccess(BookingAnalyticsResponse response) {
                runOnUiThread(() -> {
                    updateBookingAnalytics(response);
                    showProgressIndicator(false);
                });
            }

            @Override
            public void onError(String errorMessage, Throwable throwable) {
                runOnUiThread(() -> {
                    showLongToast("Error loading booking analytics: " + errorMessage);
                    Logger.e(TAG, "Error loading booking analytics", throwable);
                    showProgressIndicator(false);
                });
            }
        });
    }

    private void showProgressIndicator(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setRefreshing(false);
        } else {
            progressBar.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void updateBusinessOverview(BusinessOverviewResponse response) {
        // Update total users
        tvTotalUsers.setText(String.valueOf(response.getTotalUsers()));
        if (response.getGrowthMetrics() != null) {
            updateGrowthText(tvTotalUsersGrowth, response.getGrowthMetrics().getUserGrowthRate());
        }

        // Update total helpers
        tvTotalHelpers.setText(String.valueOf(response.getTotalHelpers()));
        if (response.getGrowthMetrics() != null) {
            updateGrowthText(tvTotalHelpersGrowth, response.getGrowthMetrics().getHelperGrowthRate());
        }

        // Update total bookings
        tvTotalBookings.setText(String.valueOf(response.getTotalBookings()));
        if (response.getGrowthMetrics() != null) {
            updateGrowthText(tvTotalBookingsGrowth, response.getGrowthMetrics().getBookingGrowthRate());
        }

        // Update total revenue
        tvTotalRevenue.setText(currencyFormat.format(response.getTotalRevenue()));
        if (response.getGrowthMetrics() != null) {
            updateGrowthText(tvTotalRevenueGrowth, response.getGrowthMetrics().getRevenueGrowthRate());
        }
    }

    private void updateGrowthText(TextView textView, double growthRate) {
        String growthText = String.format("%.1f%%", growthRate);
        textView.setText(growthText);

        // Set color based on growth rate
        if (growthRate > 0) {
            textView.setTextColor(getResources().getColor(R.color.success_color));
            textView.setText("+" + growthText);
        } else if (growthRate < 0) {
            textView.setTextColor(getResources().getColor(R.color.error_color));
        } else {
            textView.setTextColor(getResources().getColor(R.color.text_secondary));
        }
    }

    private void updateRevenueAnalytics(RevenueAnalyticsResponse response) {
        tvNetRevenue.setText(currencyFormat.format(response.getNetRevenue()));
        tvPlatformFees.setText(currencyFormat.format(response.getPlatformFees()));
        tvHelperEarnings.setText(currencyFormat.format(response.getHelperEarnings()));
        tvPaymentSuccessRate.setText(String.format("%.1f%%", response.getPaymentSuccessRate()));
    }

    private void updateServicePerformance(List<ServicePerformanceResponse> services) {
        if (services == null || services.isEmpty()) {
            rvServicePerformance.setVisibility(View.GONE);
            tvServicePerformanceEmpty.setVisibility(View.VISIBLE);
        } else {
            rvServicePerformance.setVisibility(View.VISIBLE);
            tvServicePerformanceEmpty.setVisibility(View.GONE);
            servicePerformanceAdapter.updateServices(services);
        }
    }

    private void updateHelperRankings(List<HelperRankingResponse> helpers) {
        if (helpers == null || helpers.isEmpty()) {
            rvHelperRankings.setVisibility(View.GONE);
            tvHelperRankingsEmpty.setVisibility(View.VISIBLE);
        } else {
            rvHelperRankings.setVisibility(View.VISIBLE);
            tvHelperRankingsEmpty.setVisibility(View.GONE);
            helperRankingAdapter.updateHelpers(helpers);
        }
    }

    private void updateBookingAnalytics(BookingAnalyticsResponse response) {
        tvPendingBookings.setText(String.valueOf(response.getPendingBookings()));
        tvCompletedBookings.setText(String.valueOf(response.getCompletedBookings()));
        tvCancelledBookings.setText(String.valueOf(response.getCancelledBookings()));
        tvCompletionRate.setText(String.format("%.1f%%", response.getCompletionRate()));
        tvCancellationRate.setText(String.format("%.1f%%", response.getCancellationRate()));
    }
}
