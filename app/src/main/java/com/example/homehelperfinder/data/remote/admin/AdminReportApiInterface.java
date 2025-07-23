package com.example.homehelperfinder.data.remote.admin;

import com.example.homehelperfinder.data.model.response.ApiResponse;
import com.example.homehelperfinder.data.model.response.BookingAnalyticsResponse;
import com.example.homehelperfinder.data.model.response.BusinessOverviewResponse;
import com.example.homehelperfinder.data.model.response.HelperRankingResponse;
import com.example.homehelperfinder.data.model.response.RevenueAnalyticsResponse;
import com.example.homehelperfinder.data.model.response.ServicePerformanceResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Retrofit interface for Admin Report APIs
 */
public interface AdminReportApiInterface {

    /**
     * Get business overview report
     * @return Business overview data including totals and growth metrics
     */
    @GET("Report/admin/business-overview")
    Call<ApiResponse<BusinessOverviewResponse>> getBusinessOverview();

    /**
     * Get revenue analytics report
     * @param period The period for the report (e.g., "month", "week", "year")
     * @return Revenue analytics data including trends and breakdowns
     */
    @GET("Report/admin/revenue-analytics")
    Call<ApiResponse<RevenueAnalyticsResponse>> getRevenueAnalytics(
            @Query("period") String period
    );

    /**
     * Get service performance report
     * @param period The period for the report (e.g., "month", "week", "year")
     * @return List of service performance data
     */
    @GET("Report/admin/service-performance")
    Call<ApiResponse<List<ServicePerformanceResponse>>> getServicePerformance(
            @Query("period") String period
    );

    /**
     * Get helper rankings report
     * @param count Number of top helpers to return (default: 10)
     * @param period The period for the report (e.g., "month", "week", "year")
     * @return List of helper ranking data
     */
    @GET("Report/admin/helper-rankings")
    Call<ApiResponse<List<HelperRankingResponse>>> getHelperRankings(
            @Query("count") Integer count,
            @Query("period") String period
    );

    /**
     * Get booking analytics report
     * @param serviceId Optional service ID to filter by specific service
     * @param period The period for the report (e.g., "month", "week", "year")
     * @return Booking analytics data including status breakdown and trends
     */
    @GET("Report/admin/booking-analytics")
    Call<ApiResponse<BookingAnalyticsResponse>> getBookingAnalytics(
            @Query("serviceId") Integer serviceId,
            @Query("period") String period
    );
}
