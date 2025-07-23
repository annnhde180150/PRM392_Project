package com.example.homehelperfinder.data.remote.admin;

import android.content.Context;

import com.example.homehelperfinder.data.model.response.ApiResponse;
import com.example.homehelperfinder.data.model.response.BookingAnalyticsResponse;
import com.example.homehelperfinder.data.model.response.BusinessOverviewResponse;
import com.example.homehelperfinder.data.model.response.HelperRankingResponse;
import com.example.homehelperfinder.data.model.response.RevenueAnalyticsResponse;
import com.example.homehelperfinder.data.model.response.ServicePerformanceResponse;
import com.example.homehelperfinder.data.remote.BaseApiService;
import com.example.homehelperfinder.data.remote.RetrofitClient;
import com.example.homehelperfinder.utils.Logger;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import retrofit2.Call;

/**
 * Service class for admin report API operations
 */
public class AdminReportApiService extends BaseApiService {
    private static final String TAG = "AdminReportApiService";
    private final AdminReportApiInterface apiInterface;

    public AdminReportApiService() {
        super();
        this.apiInterface = RetrofitClient.getAdminReportApiService();
    }

    /**
     * Get business overview report
     */
    public void getBusinessOverview(Context context, ApiCallback<BusinessOverviewResponse> callback) {
        Logger.d(TAG, "Getting business overview report");
        
        Call<ApiResponse<BusinessOverviewResponse>> call = apiInterface.getBusinessOverview();
        CompletableFuture<BusinessOverviewResponse> future = executeCall(context, call, "Get Business Overview");
        handleApiResponse(context, future, callback);
    }

    /**
     * Get revenue analytics report
     */
    public void getRevenueAnalytics(Context context, String period, ApiCallback<RevenueAnalyticsResponse> callback) {
        Logger.d(TAG, String.format("Getting revenue analytics report for period: %s", period));
        
        Call<ApiResponse<RevenueAnalyticsResponse>> call = apiInterface.getRevenueAnalytics(period);
        CompletableFuture<RevenueAnalyticsResponse> future = executeCall(context, call, "Get Revenue Analytics");
        handleApiResponse(context, future, callback);
    }

    /**
     * Get service performance report
     */
    public void getServicePerformance(Context context, String period, ApiCallback<List<ServicePerformanceResponse>> callback) {
        Logger.d(TAG, String.format("Getting service performance report for period: %s", period));
        
        Call<ApiResponse<List<ServicePerformanceResponse>>> call = apiInterface.getServicePerformance(period);
        CompletableFuture<List<ServicePerformanceResponse>> future = executeCall(context, call, "Get Service Performance");
        handleApiResponse(context, future, callback);
    }

    /**
     * Get helper rankings report
     */
    public void getHelperRankings(Context context, Integer count, String period, ApiCallback<List<HelperRankingResponse>> callback) {
        Logger.d(TAG, String.format("Getting helper rankings report - count: %d, period: %s", count, period));
        
        Call<ApiResponse<List<HelperRankingResponse>>> call = apiInterface.getHelperRankings(count, period);
        CompletableFuture<List<HelperRankingResponse>> future = executeCall(context, call, "Get Helper Rankings");
        handleApiResponse(context, future, callback);
    }

    /**
     * Get booking analytics report
     */
    public void getBookingAnalytics(Context context, Integer serviceId, String period, ApiCallback<BookingAnalyticsResponse> callback) {
        Logger.d(TAG, String.format("Getting booking analytics report - serviceId: %s, period: %s", 
                serviceId != null ? serviceId.toString() : "all", period));
        
        Call<ApiResponse<BookingAnalyticsResponse>> call = apiInterface.getBookingAnalytics(serviceId, period);
        CompletableFuture<BookingAnalyticsResponse> future = executeCall(context, call, "Get Booking Analytics");
        handleApiResponse(context, future, callback);
    }

    // Convenience methods with default parameters

    /**
     * Get revenue analytics with default period (month)
     */
    public void getRevenueAnalytics(Context context, ApiCallback<RevenueAnalyticsResponse> callback) {
        getRevenueAnalytics(context, "month", callback);
    }

    /**
     * Get service performance with default period (month)
     */
    public void getServicePerformance(Context context, ApiCallback<List<ServicePerformanceResponse>> callback) {
        getServicePerformance(context, "month", callback);
    }

    /**
     * Get helper rankings with default parameters (top 10, month period)
     */
    public void getHelperRankings(Context context, ApiCallback<List<HelperRankingResponse>> callback) {
        getHelperRankings(context, 10, "month", callback);
    }

    /**
     * Get booking analytics with default parameters (all services, month period)
     */
    public void getBookingAnalytics(Context context, ApiCallback<BookingAnalyticsResponse> callback) {
        getBookingAnalytics(context, null, "month", callback);
    }

    /**
     * Get booking analytics for specific service with default period (month)
     */
    public void getBookingAnalyticsByService(Context context, int serviceId, ApiCallback<BookingAnalyticsResponse> callback) {
        getBookingAnalytics(context, serviceId, "month", callback);
    }
}
