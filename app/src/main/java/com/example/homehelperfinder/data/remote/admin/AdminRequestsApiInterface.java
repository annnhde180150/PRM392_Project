package com.example.homehelperfinder.data.remote.admin;

import com.example.homehelperfinder.data.model.response.AdminRequestsResponse;
import com.example.homehelperfinder.data.model.response.ApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Retrofit interface for admin requests API
 */
public interface AdminRequestsApiInterface {
    
    /**
     * Get admin requests with filtering and pagination
     * 
     * @param status Filter by request status (optional)
     * @param user Filter by user name/email (optional)
     * @param dateFrom Filter by date from (optional, format: yyyy-MM-dd'T'HH:mm:ss.SSS'Z')
     * @param dateTo Filter by date to (optional, format: yyyy-MM-dd'T'HH:mm:ss.SSS'Z')
     * @param location Filter by location (optional)
     * @param page Page number (default: 1)
     * @param pageSize Number of items per page (default: 20)
     * @return Call with AdminRequestsResponse wrapped in ApiResponse
     */
    @GET("admin/requests")
    Call<ApiResponse<AdminRequestsResponse>> getAdminRequests(
            @Query("status") String status,
            @Query("user") String user,
            @Query("dateFrom") String dateFrom,
            @Query("dateTo") String dateTo,
            @Query("location") String location,
            @Query("page") int page,
            @Query("pageSize") int pageSize
    );
}
