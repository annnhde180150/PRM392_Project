package com.example.homehelperfinder.ui.reports.helper.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.homehelperfinder.R;
import com.example.homehelperfinder.data.model.ReportPeriod;
import com.example.homehelperfinder.data.model.response.HelperEarningsReportResponse;
import com.example.homehelperfinder.data.remote.BaseApiService;
import com.example.homehelperfinder.data.remote.report.HelperReportApiService;
import com.example.homehelperfinder.ui.reports.helper.HelperReportsActivity;
import com.example.homehelperfinder.ui.reports.helper.adapters.EarningsTrendAdapter;
import com.example.homehelperfinder.ui.reports.helper.adapters.ServiceBreakdownAdapter;
import com.example.homehelperfinder.utils.Logger;
import com.google.android.material.card.MaterialCardView;

/**
 * Fragment for displaying helper earnings report
 */
public class HelperEarningsReportFragment extends Fragment implements HelperReportsActivity.OnPeriodChangeListener {
    private static final String TAG = "HelperEarningsReportFragment";

    // UI Components
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;
    private MaterialCardView statsCard;
    private MaterialCardView trendsCard;
    private MaterialCardView servicesCard;

    // Statistics TextViews
    private TextView tvHelperName;
    private TextView tvTotalEarnings;
    private TextView tvTotalBookings;
    private TextView tvCompletedBookings;
    private TextView tvCompletionRate;
    private TextView tvAverageRating;
    private TextView tvTotalHours;
    private TextView tvAverageBookingValue;

    // RecyclerViews
    private RecyclerView recyclerViewTrends;
    private RecyclerView recyclerViewServices;

    // Adapters
    private EarningsTrendAdapter trendsAdapter;
    private ServiceBreakdownAdapter servicesAdapter;

    // API Service
    private HelperReportApiService apiService;

    // Current period
    private ReportPeriod currentPeriod = ReportPeriod.MONTH;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_helper_earnings_report, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        Logger.d(TAG, "HelperEarningsReportFragment view created");
        
        initViews(view);
        setupRecyclerViews();
        setupSwipeRefresh();
        initApiService();
        loadEarningsReport();
    }

    private void initViews(View view) {
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        progressBar = view.findViewById(R.id.progress_bar);
        statsCard = view.findViewById(R.id.stats_card);
        trendsCard = view.findViewById(R.id.trends_card);
        servicesCard = view.findViewById(R.id.services_card);

        // Statistics TextViews
        tvHelperName = view.findViewById(R.id.tv_helper_name);
        tvTotalEarnings = view.findViewById(R.id.tv_total_earnings);
        tvTotalBookings = view.findViewById(R.id.tv_total_bookings);
        tvCompletedBookings = view.findViewById(R.id.tv_completed_bookings);
        tvCompletionRate = view.findViewById(R.id.tv_completion_rate);
        tvAverageRating = view.findViewById(R.id.tv_average_rating);
        tvTotalHours = view.findViewById(R.id.tv_total_hours);
        tvAverageBookingValue = view.findViewById(R.id.tv_average_booking_value);

        // RecyclerViews
        recyclerViewTrends = view.findViewById(R.id.recycler_view_trends);
        recyclerViewServices = view.findViewById(R.id.recycler_view_services);
    }

    private void setupRecyclerViews() {
        // Trends RecyclerView
        trendsAdapter = new EarningsTrendAdapter();
        recyclerViewTrends.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewTrends.setAdapter(trendsAdapter);
        recyclerViewTrends.setNestedScrollingEnabled(false);

        // Services RecyclerView
        servicesAdapter = new ServiceBreakdownAdapter();
        recyclerViewServices.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewServices.setAdapter(servicesAdapter);
        recyclerViewServices.setNestedScrollingEnabled(false);
    }

    private void setupSwipeRefresh() {
        swipeRefreshLayout.setColorSchemeResources(R.color.primary);
        swipeRefreshLayout.setOnRefreshListener(this::loadEarningsReport);
    }

    private void initApiService() {
        apiService = new HelperReportApiService();
    }

    private void loadEarningsReport() {
        Logger.d(TAG, "Loading earnings report for period: " + currentPeriod.getValue());
        
        showLoading(true);
        
        apiService.getHelperEarningsReport(getContext(), currentPeriod.getValue(), new BaseApiService.ApiCallback<HelperEarningsReportResponse>() {
            @Override
            public void onSuccess(HelperEarningsReportResponse data) {
                Logger.i(TAG, "Earnings report loaded successfully");
                showLoading(false);
                displayEarningsData(data);
            }

            @Override
            public void onError(String errorMessage, Throwable throwable) {
                Logger.e(TAG, "Failed to load earnings report: " + errorMessage, throwable);
                showLoading(false);
                showError("Không thể tải báo cáo thu nhập: " + errorMessage);
            }
        });
    }

    private void displayEarningsData(HelperEarningsReportResponse data) {
        if (data == null) {
            showError("Không có dữ liệu báo cáo");
            return;
        }

        // Display basic statistics
        tvHelperName.setText(data.getHelperName());
        tvTotalEarnings.setText(data.getFormattedTotalEarnings());
        tvTotalBookings.setText(String.valueOf(data.getTotalBookings()));
        tvCompletedBookings.setText(String.valueOf(data.getCompletedBookings()));
        tvCompletionRate.setText(data.getFormattedCompletionRate());
        tvAverageRating.setText(data.getFormattedAverageRating());
        tvTotalHours.setText(data.getFormattedTotalHours());
        tvAverageBookingValue.setText(data.getFormattedAverageBookingValue());

        // Update adapters
        if (data.hasEarningsTrend()) {
            trendsAdapter.updateData(data.getEarningsTrend());
            trendsCard.setVisibility(View.VISIBLE);
        } else {
            trendsCard.setVisibility(View.GONE);
        }

        if (data.hasServiceBreakdown()) {
            servicesAdapter.updateData(data.getServiceBreakdown());
            servicesCard.setVisibility(View.VISIBLE);
        } else {
            servicesCard.setVisibility(View.GONE);
        }
    }

    private void showLoading(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setRefreshing(true);
        } else {
            progressBar.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void showError(String message) {
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onPeriodChanged(ReportPeriod period) {
        Logger.d(TAG, "Period changed to: " + period.getValue());
        this.currentPeriod = period;
        loadEarningsReport();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Logger.d(TAG, "HelperEarningsReportFragment view destroyed");
    }
}
