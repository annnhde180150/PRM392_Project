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
import com.example.homehelperfinder.data.model.response.CustomerSpendingReportResponse;
import com.example.homehelperfinder.data.repository.CustomerReportRepository;
import com.example.homehelperfinder.ui.reports.CustomerReportsActivity;
import com.example.homehelperfinder.ui.reports.adapters.SpendingTrendAdapter;
import com.example.homehelperfinder.utils.ApiHelper;
import com.example.homehelperfinder.utils.Logger;
import com.google.android.material.card.MaterialCardView;

/**
 * Fragment for displaying spending report data
 */
public class SpendingReportFragment extends Fragment implements CustomerReportsActivity.OnPeriodChangeListener {

    private static final String TAG = "SpendingReportFragment";

    // Views
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;
    private MaterialCardView statsCard;
    private TextView tvTotalSpent, tvAverageSpending, tvTotalTransactions, tvPeriodInfo;
    private RecyclerView recyclerViewTrend;
    private TextView tvNoData;

    // Data
    private CustomerReportRepository repository;
    private SpendingTrendAdapter trendAdapter;
    private ReportPeriod currentPeriod = ReportPeriod.MONTH;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repository = new CustomerReportRepository();
        trendAdapter = new SpendingTrendAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_spending_report, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        setupRecyclerView();
        setupSwipeRefresh();
        loadSpendingReport();
    }

    private void initViews(View view) {
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        progressBar = view.findViewById(R.id.progressBar);
        statsCard = view.findViewById(R.id.statsCard);
        tvTotalSpent = view.findViewById(R.id.tvTotalSpent);
        tvAverageSpending = view.findViewById(R.id.tvAverageSpending);
        tvTotalTransactions = view.findViewById(R.id.tvTotalTransactions);
        tvPeriodInfo = view.findViewById(R.id.tvPeriodInfo);
        recyclerViewTrend = view.findViewById(R.id.recyclerViewTrend);
        tvNoData = view.findViewById(R.id.tvNoData);
    }

    private void setupRecyclerView() {
        recyclerViewTrend.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewTrend.setAdapter(trendAdapter);
    }

    private void setupSwipeRefresh() {
        swipeRefreshLayout.setOnRefreshListener(this::loadSpendingReport);
        swipeRefreshLayout.setColorSchemeResources(R.color.primary);
    }

    private void loadSpendingReport() {
        showLoading(true);
        
        repository.getMySpending(requireContext(), currentPeriod, 
            ApiHelper.callback(
                this::onSpendingReportLoaded,
                this::onError
            )
        );
    }

    private void onSpendingReportLoaded(CustomerSpendingReportResponse response) {
        showLoading(false);
        
        if (response != null) {
            updateUI(response);
            Logger.d(TAG, "Spending report loaded successfully");
        } else {
            showNoData(true);
            Logger.w(TAG, "Spending report response is null");
        }
    }

    private void onError(String errorMessage, Throwable throwable) {
        showLoading(false);
        showNoData(true);
        
        if (getContext() != null) {
            Toast.makeText(getContext(), "Lỗi tải báo cáo: " + errorMessage, Toast.LENGTH_SHORT).show();
        }
        
        Logger.e(TAG, "Error loading spending report: " + errorMessage, throwable);
    }

    private void updateUI(CustomerSpendingReportResponse response) {
        showNoData(!response.hasSpending());
        
        if (response.hasSpending()) {
            // Update statistics
            tvTotalSpent.setText(response.getFormattedTotalSpent());
            tvAverageSpending.setText(response.getFormattedAverageSpending());
            tvTotalTransactions.setText(String.valueOf(response.getTotalTransactions()));
            
            // Update period info
            if (response.hasPeriod()) {
                tvPeriodInfo.setText(response.getPeriod().getFormattedPeriod());
            }

            // Update trend data
            if (response.hasSpendingTrend()) {
                trendAdapter.updateData(response.getSpendingTrend());
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
        loadSpendingReport();
        Logger.d(TAG, "Period changed to: " + period.getDisplayName());
    }
}
