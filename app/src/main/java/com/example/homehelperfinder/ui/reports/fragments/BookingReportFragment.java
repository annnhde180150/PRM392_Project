package com.example.homehelperfinder.ui.reports.fragments;

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
import com.example.homehelperfinder.data.model.response.CustomerBookingReportResponse;
import com.example.homehelperfinder.data.repository.CustomerReportRepository;
import com.example.homehelperfinder.ui.reports.CustomerReportsActivity;
import com.example.homehelperfinder.ui.reports.adapters.BookingTrendAdapter;
import com.example.homehelperfinder.utils.ApiHelper;
import com.example.homehelperfinder.utils.Logger;
import com.google.android.material.card.MaterialCardView;

/**
 * Fragment for displaying booking report data
 */
public class BookingReportFragment extends Fragment implements CustomerReportsActivity.OnPeriodChangeListener {

    private static final String TAG = "BookingReportFragment";

    // Views
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;
    private MaterialCardView statsCard;
    private TextView tvTotalBookings, tvActiveBookings, tvCompletedBookings, tvCancelledBookings;
    private TextView tvCompletionRate, tvCancellationRate, tvAverageValue, tvTotalValue;
    private RecyclerView recyclerViewTrend;
    private TextView tvNoData;

    // Data
    private CustomerReportRepository repository;
    private BookingTrendAdapter trendAdapter;
    private ReportPeriod currentPeriod = ReportPeriod.MONTH;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repository = new CustomerReportRepository();
        trendAdapter = new BookingTrendAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_booking_report, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        setupRecyclerView();
        setupSwipeRefresh();
        loadBookingReport();
    }

    private void initViews(View view) {
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        progressBar = view.findViewById(R.id.progressBar);
        statsCard = view.findViewById(R.id.statsCard);
        tvTotalBookings = view.findViewById(R.id.tvTotalBookings);
        tvActiveBookings = view.findViewById(R.id.tvActiveBookings);
        tvCompletedBookings = view.findViewById(R.id.tvCompletedBookings);
        tvCancelledBookings = view.findViewById(R.id.tvCancelledBookings);
        tvCompletionRate = view.findViewById(R.id.tvCompletionRate);
        tvCancellationRate = view.findViewById(R.id.tvCancellationRate);
        tvAverageValue = view.findViewById(R.id.tvAverageValue);
        tvTotalValue = view.findViewById(R.id.tvTotalValue);
        recyclerViewTrend = view.findViewById(R.id.recyclerViewTrend);
        tvNoData = view.findViewById(R.id.tvNoData);
    }

    private void setupRecyclerView() {
        recyclerViewTrend.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewTrend.setAdapter(trendAdapter);
    }

    private void setupSwipeRefresh() {
        swipeRefreshLayout.setOnRefreshListener(this::loadBookingReport);
        swipeRefreshLayout.setColorSchemeResources(R.color.primary);
    }

    private void loadBookingReport() {
        showLoading(true);
        
        repository.getMyBookings(requireContext(), currentPeriod, 
            ApiHelper.callback(
                this::onBookingReportLoaded,
                this::onError
            )
        );
    }

    private void onBookingReportLoaded(CustomerBookingReportResponse response) {
        showLoading(false);
        
        if (response != null) {
            updateUI(response);
            Logger.d(TAG, "Booking report loaded successfully");
        } else {
            showNoData(true);
            Logger.w(TAG, "Booking report response is null");
        }
    }

    private void onError(String errorMessage, Throwable throwable) {
        showLoading(false);
        showNoData(true);
        
        if (getContext() != null) {
            Toast.makeText(getContext(), "Lỗi tải báo cáo: " + errorMessage, Toast.LENGTH_SHORT).show();
        }
        
        Logger.e(TAG, "Error loading booking report: " + errorMessage, throwable);
    }

    private void updateUI(CustomerBookingReportResponse response) {
        showNoData(!response.hasBookings());
        
        if (response.hasBookings()) {
            // Update statistics
            tvTotalBookings.setText(String.valueOf(response.getTotalBookings()));
            tvActiveBookings.setText(String.valueOf(response.getActiveBookings()));
            tvCompletedBookings.setText(String.valueOf(response.getCompletedBookings()));
            tvCancelledBookings.setText(String.valueOf(response.getCancelledBookings()));
            tvCompletionRate.setText(response.getFormattedCompletionRate());
            tvCancellationRate.setText(response.getFormattedCancellationRate());
            tvAverageValue.setText(response.getFormattedAverageBookingValue());
            tvTotalValue.setText(response.getFormattedTotalBookingValue());

            // Update trend data
            if (response.hasBookingTrend()) {
                trendAdapter.updateData(response.getBookingTrend());
                recyclerViewTrend.setVisibility(View.VISIBLE);
            } else {
                recyclerViewTrend.setVisibility(View.GONE);
            }
        }
    }

    private void showLoading(boolean show) {
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(show);
        } else {
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        }
        statsCard.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private void showNoData(boolean show) {
        tvNoData.setVisibility(show ? View.VISIBLE : View.GONE);
        statsCard.setVisibility(show ? View.GONE : View.VISIBLE);
        recyclerViewTrend.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onPeriodChanged(ReportPeriod period) {
        this.currentPeriod = period;
        loadBookingReport();
        Logger.d(TAG, "Period changed to: " + period.getDisplayName());
    }
}
