package com.example.homehelperfinder.data.remote.admin;

import com.example.homehelperfinder.data.model.HelperApplicationDetail;
import com.example.homehelperfinder.data.model.request.ApplicationDecisionRequest;
import com.example.homehelperfinder.data.model.response.ApiResponse;
import com.example.homehelperfinder.data.model.response.ApplicationDecisionResponse;
import com.example.homehelperfinder.data.model.response.HelperApplicationsResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Retrofit interface for helper applications API endpoints
 */
public interface HelperApplicationsApiInterface {
    
    /**
     * Get helper applications with filtering and pagination
     * 
     * @param status Filter by approval status (optional)
     *               Valid values: "pending", "approved", "rejected", "revision_requested"
     * @param page Page number (default: 1)
     * @param pageSize Number of items per page (default: 20)
     * @return Call with HelperApplicationsResponse wrapped in ApiResponse
     */
    @GET("admin/helpers/applications")
    Call<ApiResponse<HelperApplicationsResponse>> getHelperApplications(
            @Query("status") String status,
            @Query("page") int page,
            @Query("pageSize") int pageSize
    );
    
    /**
     * Get detailed helper application by ID
     * 
     * @param helperId The ID of the helper application to retrieve
     * @return Call with HelperApplicationDetail wrapped in ApiResponse
     */
    @GET("admin/helpers/applications/{id}")
    Call<ApiResponse<HelperApplicationDetail>> getHelperApplicationDetail(
            @Path("id") int helperId
    );
    
    /**
     * Make a decision on helper application (approve/reject/request revision)
     * 
     * @param helperId The ID of the helper application
     * @param request The decision request containing status and optional comment
     * @return Call with ApplicationDecisionResponse wrapped in ApiResponse
     */
    @POST("admin/helpers/applications/{id}/decision")
    Call<ApiResponse<ApplicationDecisionResponse>> makeApplicationDecision(
            @Path("id") int helperId,
            @Body ApplicationDecisionRequest request
    );
}
