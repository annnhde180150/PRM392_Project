package com.example.homehelperfinder.data.remote.report;

import com.example.homehelperfinder.data.model.response.ApiResponse;
import com.example.homehelperfinder.data.model.response.CustomerBookingReportResponse;
import com.example.homehelperfinder.data.model.response.CustomerSpendingReportResponse;
import com.example.homehelperfinder.data.model.response.FavoriteHelperReportResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * API interface for customer report endpoints
 */
public interface CustomerReportApiInterface {

    /**
     * Get customer booking report with period filter
     * @param period Report period: day, week, month, quarter, year
     * @return Customer booking report data
     */
    @GET("Report/customer/my-bookings")
    Call<ApiResponse<CustomerBookingReportResponse>> getMyBookings(
            @Query("period") String period
    );

    /**
     * Get customer spending report with period filter
     * @param period Report period: day, week, month, quarter, year
     * @return Customer spending report data
     */
    @GET("Report/customer/my-spending")
    Call<ApiResponse<CustomerSpendingReportResponse>> getMySpending(
            @Query("period") String period
    );

    /**
     * Get favorite helpers report
     * @return List of favorite helpers with detailed analytics
     */
    @GET("Report/customer/favorite-helpers")
    Call<ApiResponse<List<FavoriteHelperReportResponse>>> getFavoriteHelpers();
}
