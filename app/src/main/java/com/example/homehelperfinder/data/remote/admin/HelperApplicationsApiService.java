package com.example.homehelperfinder.data.remote.admin;

import android.content.Context;

import com.example.homehelperfinder.data.model.HelperApplicationDetail;
import com.example.homehelperfinder.data.model.request.ApplicationDecisionRequest;
import com.example.homehelperfinder.data.model.response.ApiResponse;
import com.example.homehelperfinder.data.model.response.ApplicationDecisionResponse;
import com.example.homehelperfinder.data.model.response.HelperApplicationsResponse;
import com.example.homehelperfinder.data.remote.BaseApiService;
import com.example.homehelperfinder.data.remote.RetrofitClient;
import com.example.homehelperfinder.utils.Logger;

import java.util.concurrent.CompletableFuture;

import retrofit2.Call;

/**
 * Service class for helper applications API operations
 */
public class HelperApplicationsApiService extends BaseApiService {
    private static final String TAG = "HelperApplicationsApiService";
    private final HelperApplicationsApiInterface apiInterface;

    public HelperApplicationsApiService() {
        this.apiInterface = RetrofitClient.getHelperApplicationsApiService();
    }

    /**
     * Get helper applications with filtering and pagination
     */
    public void getHelperApplications(Context context, String status, int page, int pageSize,
                                    ApiCallback<HelperApplicationsResponse> callback) {
        
        Logger.d(TAG, String.format("Getting helper applications - page: %d, pageSize: %d, status: %s", 
                page, pageSize, status));
        
        Call<ApiResponse<HelperApplicationsResponse>> call = apiInterface.getHelperApplications(
                status, page, pageSize);
        
        CompletableFuture<HelperApplicationsResponse> future = executeCall(context, call, "Get Helper Applications");
        handleApiResponse(context, future, callback);
    }

    /**
     * Get helper applications with default pagination
     */
    public void getHelperApplications(Context context, ApiCallback<HelperApplicationsResponse> callback) {
        getHelperApplications(context, null, 1, 20, callback);
    }

    /**
     * Get helper applications by status
     */
    public void getHelperApplicationsByStatus(Context context, String status, int page, int pageSize,
                                            ApiCallback<HelperApplicationsResponse> callback) {
        getHelperApplications(context, status, page, pageSize, callback);
    }

    /**
     * Get pending helper applications
     */
    public void getPendingApplications(Context context, int page, int pageSize,
                                     ApiCallback<HelperApplicationsResponse> callback) {
        getHelperApplications(context, "pending", page, pageSize, callback);
    }

    /**
     * Get approved helper applications
     */
    public void getApprovedApplications(Context context, int page, int pageSize,
                                      ApiCallback<HelperApplicationsResponse> callback) {
        getHelperApplications(context, "approved", page, pageSize, callback);
    }

    /**
     * Get rejected helper applications
     */
    public void getRejectedApplications(Context context, int page, int pageSize,
                                      ApiCallback<HelperApplicationsResponse> callback) {
        getHelperApplications(context, "rejected", page, pageSize, callback);
    }

    /**
     * Get helper applications with revision requested
     */
    public void getRevisionRequestedApplications(Context context, int page, int pageSize,
                                               ApiCallback<HelperApplicationsResponse> callback) {
        getHelperApplications(context, "revision_requested", page, pageSize, callback);
    }

    /**
     * Get detailed helper application by ID
     */
    public void getHelperApplicationDetail(Context context, int helperId,
                                         ApiCallback<HelperApplicationDetail> callback) {
        
        Logger.d(TAG, "Getting helper application detail for ID: " + helperId);
        
        Call<ApiResponse<HelperApplicationDetail>> call = apiInterface.getHelperApplicationDetail(helperId);
        
        CompletableFuture<HelperApplicationDetail> future = executeCall(context, call, "Get Helper Application Detail");
        handleApiResponse(context, future, callback);
    }

    /**
     * Make a decision on helper application
     */
    public void makeApplicationDecision(Context context, int helperId, ApplicationDecisionRequest request,
                                      ApiCallback<ApplicationDecisionResponse> callback) {
        
        Logger.d(TAG, String.format("Making decision for helper %d: %s", helperId, request.getStatus()));
        
        Call<ApiResponse<ApplicationDecisionResponse>> call = apiInterface.makeApplicationDecision(helperId, request);
        
        CompletableFuture<ApplicationDecisionResponse> future = executeCall(context, call, "Make Application Decision");
        handleApiResponse(context, future, callback);
    }

    /**
     * Approve helper application
     */
    public void approveApplication(Context context, int helperId, String comment,
                                 ApiCallback<ApplicationDecisionResponse> callback) {
        ApplicationDecisionRequest request = ApplicationDecisionRequest.approve(comment);
        makeApplicationDecision(context, helperId, request, callback);
    }

    /**
     * Approve helper application without comment
     */
    public void approveApplication(Context context, int helperId,
                                 ApiCallback<ApplicationDecisionResponse> callback) {
        approveApplication(context, helperId, null, callback);
    }

    /**
     * Reject helper application
     */
    public void rejectApplication(Context context, int helperId, String comment,
                                ApiCallback<ApplicationDecisionResponse> callback) {
        ApplicationDecisionRequest request = ApplicationDecisionRequest.reject(comment);
        makeApplicationDecision(context, helperId, request, callback);
    }

    /**
     * Request revision for helper application
     */
    public void requestRevision(Context context, int helperId, String comment,
                              ApiCallback<ApplicationDecisionResponse> callback) {
        ApplicationDecisionRequest request = ApplicationDecisionRequest.requestRevision(comment);
        makeApplicationDecision(context, helperId, request, callback);
    }
}
