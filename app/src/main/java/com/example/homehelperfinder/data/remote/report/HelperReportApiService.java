package com.example.homehelperfinder.data.remote.report;

import android.content.Context;

import com.example.homehelperfinder.data.model.response.HelperEarningsReportResponse;
import com.example.homehelperfinder.data.model.response.HelperScheduleAnalyticsResponse;
import com.example.homehelperfinder.data.remote.BaseApiService;
import com.example.homehelperfinder.data.remote.RetrofitClient;
import com.example.homehelperfinder.utils.Logger;

import java.util.concurrent.CompletableFuture;

/**
 * Service class for Helper Report APIs
 * Extends BaseApiService for consistent error handling and logging
 */
public class HelperReportApiService extends BaseApiService {
    private static final String TAG = "HelperReportApiService";
    private final HelperReportApiInterface apiInterface;

    public HelperReportApiService() {
        this.apiInterface = RetrofitClient.getHelperReportApiService();
    }

    /**
     * Get helper earnings report
     * @param context Application context
     * @param period Report period (month, week, year)
     * @return CompletableFuture with earnings report data
     */
    public CompletableFuture<HelperEarningsReportResponse> getHelperEarningsReport(
            Context context, 
            String period) {
        Logger.d(TAG, "Getting helper earnings report for period: " + period);
        return executeCall(context, apiInterface.getHelperEarningsReport(period), "getHelperEarningsReport");
    }

    /**
     * Get helper earnings report with callback
     * @param context Application context
     * @param period Report period (month, week, year)
     * @param callback Callback for handling response
     */
    public void getHelperEarningsReport(
            Context context, 
            String period, 
            ApiCallback<HelperEarningsReportResponse> callback) {
        handleApiResponse(context, getHelperEarningsReport(context, period), callback);
    }

    /**
     * Get helper schedule analytics
     * @param context Application context
     * @param period Analytics period (month, week, year)
     * @return CompletableFuture with schedule analytics data
     */
    public CompletableFuture<HelperScheduleAnalyticsResponse> getHelperScheduleAnalytics(
            Context context, 
            String period) {
        Logger.d(TAG, "Getting helper schedule analytics for period: " + period);
        return executeCall(context, apiInterface.getHelperScheduleAnalytics(period), "getHelperScheduleAnalytics");
    }

    /**
     * Get helper schedule analytics with callback
     * @param context Application context
     * @param period Analytics period (month, week, year)
     * @param callback Callback for handling response
     */
    public void getHelperScheduleAnalytics(
            Context context, 
            String period, 
            ApiCallback<HelperScheduleAnalyticsResponse> callback) {
        handleApiResponse(context, getHelperScheduleAnalytics(context, period), callback);
    }

    /**
     * Get helper earnings report with default period (month)
     * @param context Application context
     * @return CompletableFuture with earnings report data
     */
    public CompletableFuture<HelperEarningsReportResponse> getHelperEarningsReport(Context context) {
        return getHelperEarningsReport(context, "month");
    }

    /**
     * Get helper earnings report with default period (month) and callback
     * @param context Application context
     * @param callback Callback for handling response
     */
    public void getHelperEarningsReport(Context context, ApiCallback<HelperEarningsReportResponse> callback) {
        getHelperEarningsReport(context, "month", callback);
    }

    /**
     * Get helper schedule analytics with default period (month)
     * @param context Application context
     * @return CompletableFuture with schedule analytics data
     */
    public CompletableFuture<HelperScheduleAnalyticsResponse> getHelperScheduleAnalytics(Context context) {
        return getHelperScheduleAnalytics(context, "month");
    }

    /**
     * Get helper schedule analytics with default period (month) and callback
     * @param context Application context
     * @param callback Callback for handling response
     */
    public void getHelperScheduleAnalytics(Context context, ApiCallback<HelperScheduleAnalyticsResponse> callback) {
        getHelperScheduleAnalytics(context, "month", callback);
    }
}
