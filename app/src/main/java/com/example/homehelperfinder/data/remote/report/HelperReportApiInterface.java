package com.example.homehelperfinder.data.remote.report;

import com.example.homehelperfinder.data.model.response.ApiResponse;
import com.example.homehelperfinder.data.model.response.HelperEarningsReportResponse;
import com.example.homehelperfinder.data.model.response.HelperScheduleAnalyticsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Retrofit interface for Helper Report APIs
 */
public interface HelperReportApiInterface {

    /**
     * Get helper earnings report
     * @param period The period for the report (e.g., "month", "week", "year")
     * @return Helper earnings report data
     */
    @GET("Report/helper/my-earnings")
    Call<ApiResponse<HelperEarningsReportResponse>> getHelperEarningsReport(
            @Query("period") String period
    );

    /**
     * Get helper schedule analytics
     * @param period The period for the analytics (e.g., "month", "week", "year")
     * @return Helper schedule analytics data
     */
    @GET("Report/helper/my-schedule-analytics")
    Call<ApiResponse<HelperScheduleAnalyticsResponse>> getHelperScheduleAnalytics(
            @Query("period") String period
    );
}
