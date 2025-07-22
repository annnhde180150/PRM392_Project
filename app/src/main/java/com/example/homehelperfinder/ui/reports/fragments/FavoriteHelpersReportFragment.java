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
import com.example.homehelperfinder.data.model.response.FavoriteHelperReportResponse;
import com.example.homehelperfinder.data.repository.CustomerReportRepository;
import com.example.homehelperfinder.ui.reports.CustomerReportsActivity;
import com.example.homehelperfinder.ui.reports.adapters.FavoriteHelpersReportAdapter;
import com.example.homehelperfinder.utils.ApiHelper;
import com.example.homehelperfinder.utils.Logger;

import java.util.List;

/**
 * Fragment for displaying favorite helpers report data
 */
public class FavoriteHelpersReportFragment extends Fragment implements CustomerReportsActivity.OnPeriodChangeListener {

    private static final String TAG = "FavoriteHelpersReportFragment";

    // Views
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;
    private RecyclerView recyclerViewHelpers;
    private TextView tvNoData;

    // Data
    private CustomerReportRepository repository;
    private FavoriteHelpersReportAdapter helpersAdapter;
    private ReportPeriod currentPeriod = ReportPeriod.MONTH;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repository = new CustomerReportRepository();
        helpersAdapter = new FavoriteHelpersReportAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite_helpers_report, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        setupRecyclerView();
        setupSwipeRefresh();
        loadFavoriteHelpersReport();
    }

    private void initViews(View view) {
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        progressBar = view.findViewById(R.id.progressBar);
        recyclerViewHelpers = view.findViewById(R.id.recyclerViewHelpers);
        tvNoData = view.findViewById(R.id.tvNoData);
    }

    private void setupRecyclerView() {
        recyclerViewHelpers.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewHelpers.setAdapter(helpersAdapter);
    }

    private void setupSwipeRefresh() {
        swipeRefreshLayout.setOnRefreshListener(this::loadFavoriteHelpersReport);
        swipeRefreshLayout.setColorSchemeResources(R.color.primary);
    }

    private void loadFavoriteHelpersReport() {
        showLoading(true);
        
        repository.getFavoriteHelpers(requireContext(), 
            ApiHelper.callback(
                this::onFavoriteHelpersLoaded,
                this::onError
            )
        );
    }

    private void onFavoriteHelpersLoaded(List<FavoriteHelperReportResponse> helpers) {
        showLoading(false);
        
        if (helpers != null && !helpers.isEmpty()) {
            updateUI(helpers);
            Logger.d(TAG, "Favorite helpers report loaded successfully: " + helpers.size() + " helpers");
        } else {
            showNoData(true);
            Logger.w(TAG, "No favorite helpers found");
        }
    }

    private void onError(String errorMessage, Throwable throwable) {
        showLoading(false);
        showNoData(true);
        
        if (getContext() != null) {
            Toast.makeText(getContext(), "Lỗi tải báo cáo: " + errorMessage, Toast.LENGTH_SHORT).show();
        }
        
        Logger.e(TAG, "Error loading favorite helpers report: " + errorMessage, throwable);
    }

    private void updateUI(List<FavoriteHelperReportResponse> helpers) {
        showNoData(false);
        helpersAdapter.updateData(helpers);
        recyclerViewHelpers.setVisibility(View.VISIBLE);
    }

    private void showLoading(boolean show) {
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(show);
        } else {
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        }
        recyclerViewHelpers.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private void showNoData(boolean show) {
        tvNoData.setVisibility(show ? View.VISIBLE : View.GONE);
        recyclerViewHelpers.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onPeriodChanged(ReportPeriod period) {
        this.currentPeriod = period;
        // Note: Favorite helpers API doesn't use period parameter, but we keep this for consistency
        Logger.d(TAG, "Period changed to: " + period.getDisplayName() + " (not used for favorite helpers)");
    }
}
