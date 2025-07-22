package com.example.homehelperfinder.data.remote.report;

import android.content.Context;

import com.example.homehelperfinder.data.model.ReportPeriod;
import com.example.homehelperfinder.data.model.response.CustomerBookingReportResponse;
import com.example.homehelperfinder.data.model.response.CustomerSpendingReportResponse;
import com.example.homehelperfinder.data.model.response.FavoriteHelperReportResponse;
import com.example.homehelperfinder.data.remote.BaseApiService;
import com.example.homehelperfinder.data.remote.RetrofitClient;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * API service for customer report endpoints
 */
public class CustomerReportApiService extends BaseApiService {

    private final CustomerReportApiInterface apiInterface;

    public CustomerReportApiService() {
        super();
        this.apiInterface = RetrofitClient.getCustomerReportApiService();
    }

    /**
     * Get customer booking report
     */
    public CompletableFuture<CustomerBookingReportResponse> getMyBookings(Context context, ReportPeriod period) {
        return executeCall(context, apiInterface.getMyBookings(period.getValue()), "getMyBookings");
    }

    public void getMyBookings(Context context, ReportPeriod period, ApiCallback<CustomerBookingReportResponse> callback) {
        handleApiResponse(context, getMyBookings(context, period), callback);
    }

    /**
     * Get customer booking report with string period
     */
    public CompletableFuture<CustomerBookingReportResponse> getMyBookings(Context context, String period) {
        return executeCall(context, apiInterface.getMyBookings(period), "getMyBookings");
    }

    public void getMyBookings(Context context, String period, ApiCallback<CustomerBookingReportResponse> callback) {
        handleApiResponse(context, getMyBookings(context, period), callback);
    }

    /**
     * Get customer spending report
     */
    public CompletableFuture<CustomerSpendingReportResponse> getMySpending(Context context, ReportPeriod period) {
        return executeCall(context, apiInterface.getMySpending(period.getValue()), "getMySpending");
    }

    public void getMySpending(Context context, ReportPeriod period, ApiCallback<CustomerSpendingReportResponse> callback) {
        handleApiResponse(context, getMySpending(context, period), callback);
    }

    /**
     * Get customer spending report with string period
     */
    public CompletableFuture<CustomerSpendingReportResponse> getMySpending(Context context, String period) {
        return executeCall(context, apiInterface.getMySpending(period), "getMySpending");
    }

    public void getMySpending(Context context, String period, ApiCallback<CustomerSpendingReportResponse> callback) {
        handleApiResponse(context, getMySpending(context, period), callback);
    }

    /**
     * Get favorite helpers report
     */
    public CompletableFuture<List<FavoriteHelperReportResponse>> getFavoriteHelpers(Context context) {
        return executeCall(context, apiInterface.getFavoriteHelpers(), "getFavoriteHelpers");
    }

    public void getFavoriteHelpers(Context context, ApiCallback<List<FavoriteHelperReportResponse>> callback) {
        handleApiResponse(context, getFavoriteHelpers(context), callback);
    }
}
