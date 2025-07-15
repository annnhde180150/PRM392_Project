package com.example.homehelperfinder.data.remote.admin;

import android.content.Context;

import com.example.homehelperfinder.data.model.response.AdminRequestsResponse;
import com.example.homehelperfinder.data.model.response.ApiResponse;
import com.example.homehelperfinder.data.remote.BaseApiService;
import com.example.homehelperfinder.data.remote.RetrofitClient;
import com.example.homehelperfinder.utils.Logger;

import java.util.concurrent.CompletableFuture;

import retrofit2.Call;

/**
 * Service class for admin requests API operations
 */
public class AdminRequestsApiService extends BaseApiService {
    private static final String TAG = "AdminRequestsApiService";
    private final AdminRequestsApiInterface apiInterface;

    public AdminRequestsApiService() {
        this.apiInterface = RetrofitClient.getAdminRequestsApiService();
    }

    /**
     * Get admin requests with filtering and pagination
     */
    public void getAdminRequests(Context context, String status, String user, 
                               String dateFrom, String dateTo, String location,
                               int page, int pageSize, ApiCallback<AdminRequestsResponse> callback) {
        
        Logger.d(TAG, String.format("Getting admin requests - page: %d, pageSize: %d, status: %s", 
                page, pageSize, status));
        
        Call<ApiResponse<AdminRequestsResponse>> call = apiInterface.getAdminRequests(
                status, user, dateFrom, dateTo, location, page, pageSize);
        
        CompletableFuture<AdminRequestsResponse> future = executeCall(context, call, "Get Admin Requests");
        handleApiResponse(context, future, callback);
    }

    /**
     * Get admin requests with default pagination
     */
    public void getAdminRequests(Context context, ApiCallback<AdminRequestsResponse> callback) {
        getAdminRequests(context, null, null, null, null, null, 1, 20, callback);
    }

    /**
     * Get admin requests with status filter
     */
    public void getAdminRequestsByStatus(Context context, String status, int page, int pageSize,
                                       ApiCallback<AdminRequestsResponse> callback) {
        getAdminRequests(context, status, null, null, null, null, page, pageSize, callback);
    }

    /**
     * Search admin requests by user
     */
    public void searchAdminRequestsByUser(Context context, String user, int page, int pageSize,
                                        ApiCallback<AdminRequestsResponse> callback) {
        getAdminRequests(context, null, user, null, null, null, page, pageSize, callback);
    }

    /**
     * Get admin requests by date range
     */
    public void getAdminRequestsByDateRange(Context context, String dateFrom, String dateTo,
                                          int page, int pageSize, ApiCallback<AdminRequestsResponse> callback) {
        getAdminRequests(context, null, null, dateFrom, dateTo, null, page, pageSize, callback);
    }
}
