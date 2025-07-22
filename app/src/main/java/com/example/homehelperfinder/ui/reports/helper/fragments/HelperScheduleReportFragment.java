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
import com.example.homehelperfinder.data.model.response.HelperScheduleAnalyticsResponse;
import com.example.homehelperfinder.data.remote.BaseApiService;
import com.example.homehelperfinder.data.remote.report.HelperReportApiService;
import com.example.homehelperfinder.ui.reports.helper.HelperReportsActivity;
import com.example.homehelperfinder.ui.reports.helper.adapters.BookingTrendAdapter;
import com.example.homehelperfinder.ui.reports.helper.adapters.EarningsTrendAdapter;
import com.example.homehelperfinder.utils.Logger;
import com.google.android.material.card.MaterialCardView;

/**
 * Fragment for displaying helper schedule analytics
 */
public class HelperScheduleReportFragment extends Fragment implements HelperReportsActivity.OnPeriodChangeListener {
    private static final String TAG = "HelperScheduleReportFragment";

    // UI Components
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;
    private MaterialCardView statsCard;
    private MaterialCardView bookingTrendsCard;
    private MaterialCardView earningsTrendsCard;

    // Statistics TextViews
    private TextView tvTotalHours;
    private TextView tvAverageHoursPerDay;
    private TextView tvTotalBookings;
    private TextView tvTotalEarnings;
    private TextView tvAverageBookingValue;
    private TextView tvHourlyRate;

    // RecyclerViews
    private RecyclerView recyclerViewBookingTrends;
    private RecyclerView recyclerViewEarningsTrends;

    // Adapters
    private BookingTrendAdapter bookingTrendsAdapter;
    private EarningsTrendAdapter earningsTrendsAdapter;

    // API Service
    private HelperReportApiService apiService;

    // Current period
    private ReportPeriod currentPeriod = ReportPeriod.MONTH;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_helper_schedule_report, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        Logger.d(TAG, "HelperScheduleReportFragment view created");
        
        initViews(view);
        setupRecyclerViews();
        setupSwipeRefresh();
        initApiService();
        loadScheduleAnalytics();
    }

    private void initViews(View view) {
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        progressBar = view.findViewById(R.id.progress_bar);
        statsCard = view.findViewById(R.id.stats_card);
        bookingTrendsCard = view.findViewById(R.id.booking_trends_card);
        earningsTrendsCard = view.findViewById(R.id.earnings_trends_card);

        // Statistics TextViews
        tvTotalHours = view.findViewById(R.id.tv_total_hours);
        tvAverageHoursPerDay = view.findViewById(R.id.tv_average_hours_per_day);
        tvTotalBookings = view.findViewById(R.id.tv_total_bookings);
        tvTotalEarnings = view.findViewById(R.id.tv_total_earnings);
        tvAverageBookingValue = view.findViewById(R.id.tv_average_booking_value);
        tvHourlyRate = view.findViewById(R.id.tv_hourly_rate);

        // RecyclerViews
        recyclerViewBookingTrends = view.findViewById(R.id.recycler_view_booking_trends);
        recyclerViewEarningsTrends = view.findViewById(R.id.recycler_view_earnings_trends);
    }

    private void setupRecyclerViews() {
        // Booking Trends RecyclerView
        bookingTrendsAdapter = new BookingTrendAdapter();
        recyclerViewBookingTrends.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewBookingTrends.setAdapter(bookingTrendsAdapter);
        recyclerViewBookingTrends.setNestedScrollingEnabled(false);

        // Earnings Trends RecyclerView
        earningsTrendsAdapter = new EarningsTrendAdapter();
        recyclerViewEarningsTrends.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewEarningsTrends.setAdapter(earningsTrendsAdapter);
        recyclerViewEarningsTrends.setNestedScrollingEnabled(false);
    }

    private void setupSwipeRefresh() {
        swipeRefreshLayout.setColorSchemeResources(R.color.primary);
        swipeRefreshLayout.setOnRefreshListener(this::loadScheduleAnalytics);
    }

    private void initApiService() {
        apiService = new HelperReportApiService();
    }

    private void loadScheduleAnalytics() {
        Logger.d(TAG, "Loading schedule analytics for period: " + currentPeriod.getValue());
        
        showLoading(true);
        
        apiService.getHelperScheduleAnalytics(getContext(), currentPeriod.getValue(), new BaseApiService.ApiCallback<HelperScheduleAnalyticsResponse>() {
            @Override
            public void onSuccess(HelperScheduleAnalyticsResponse data) {
                Logger.i(TAG, "Schedule analytics loaded successfully");
                showLoading(false);
                displayScheduleData(data);
            }

            @Override
            public void onError(String errorMessage, Throwable throwable) {
                Logger.e(TAG, "Failed to load schedule analytics: " + errorMessage, throwable);
                showLoading(false);
                showError("Không thể tải phân tích lịch làm việc: " + errorMessage);
            }
        });
    }

    private void displayScheduleData(HelperScheduleAnalyticsResponse data) {
        if (data == null) {
            showError("Không có dữ liệu phân tích");
            return;
        }

        // Display basic statistics
        tvTotalHours.setText(data.getFormattedTotalHours());
        tvAverageHoursPerDay.setText(data.getFormattedAverageHoursPerDay());
        tvTotalBookings.setText(data.getFormattedTotalBookings());
        tvTotalEarnings.setText(data.getFormattedTotalEarnings());
        tvAverageBookingValue.setText(data.getFormattedAverageBookingValue());
        tvHourlyRate.setText(data.getFormattedHourlyRate());

        // Update adapters
        if (data.hasBookingTrend()) {
            bookingTrendsAdapter.updateData(data.getBookingTrend());
            bookingTrendsCard.setVisibility(View.VISIBLE);
        } else {
            bookingTrendsCard.setVisibility(View.GONE);
        }

        if (data.hasEarningsTrend()) {
            earningsTrendsAdapter.updateData(data.getEarningsTrend());
            earningsTrendsCard.setVisibility(View.VISIBLE);
        } else {
            earningsTrendsCard.setVisibility(View.GONE);
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
        loadScheduleAnalytics();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Logger.d(TAG, "HelperScheduleReportFragment view destroyed");
    }
}
