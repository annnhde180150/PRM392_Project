package com.example.homehelperfinder.data.repository;

import android.content.Context;

import com.example.homehelperfinder.data.model.ReportPeriod;
import com.example.homehelperfinder.data.model.response.CustomerBookingReportResponse;
import com.example.homehelperfinder.data.model.response.CustomerSpendingReportResponse;
import com.example.homehelperfinder.data.model.response.FavoriteHelperReportResponse;
import com.example.homehelperfinder.data.remote.BaseApiService;
import com.example.homehelperfinder.data.remote.report.CustomerReportApiService;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Repository for customer report data
 */
public class CustomerReportRepository {
    
    private final CustomerReportApiService customerReportApiService;

    public CustomerReportRepository() {
        this.customerReportApiService = new CustomerReportApiService();
    }

    // CompletableFuture methods for booking reports
    public CompletableFuture<CustomerBookingReportResponse> getMyBookings(Context context, ReportPeriod period) {
        return customerReportApiService.getMyBookings(context, period);
    }

    public CompletableFuture<CustomerBookingReportResponse> getMyBookings(Context context, String period) {
        return customerReportApiService.getMyBookings(context, period);
    }

    // Callback methods for booking reports
    public void getMyBookings(Context context, ReportPeriod period, BaseApiService.ApiCallback<CustomerBookingReportResponse> callback) {
        customerReportApiService.getMyBookings(context, period, callback);
    }

    public void getMyBookings(Context context, String period, BaseApiService.ApiCallback<CustomerBookingReportResponse> callback) {
        customerReportApiService.getMyBookings(context, period, callback);
    }

    // CompletableFuture methods for spending reports
    public CompletableFuture<CustomerSpendingReportResponse> getMySpending(Context context, ReportPeriod period) {
        return customerReportApiService.getMySpending(context, period);
    }

    public CompletableFuture<CustomerSpendingReportResponse> getMySpending(Context context, String period) {
        return customerReportApiService.getMySpending(context, period);
    }

    // Callback methods for spending reports
    public void getMySpending(Context context, ReportPeriod period, BaseApiService.ApiCallback<CustomerSpendingReportResponse> callback) {
        customerReportApiService.getMySpending(context, period, callback);
    }

    public void getMySpending(Context context, String period, BaseApiService.ApiCallback<CustomerSpendingReportResponse> callback) {
        customerReportApiService.getMySpending(context, period, callback);
    }

    // CompletableFuture methods for favorite helpers
    public CompletableFuture<List<FavoriteHelperReportResponse>> getFavoriteHelpers(Context context) {
        return customerReportApiService.getFavoriteHelpers(context);
    }

    // Callback methods for favorite helpers
    public void getFavoriteHelpers(Context context, BaseApiService.ApiCallback<List<FavoriteHelperReportResponse>> callback) {
        customerReportApiService.getFavoriteHelpers(context, callback);
    }
}
