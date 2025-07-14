package com.example.homehelperfinder.data.remote.helper;

import android.content.Context;

import com.example.homehelperfinder.data.model.request.HelperUpdateRequest;
import com.example.homehelperfinder.data.model.response.HelperResponse;
import com.example.homehelperfinder.data.remote.BaseApiService;
import com.example.homehelperfinder.data.remote.RetrofitClient;

import java.util.concurrent.CompletableFuture;

public class HelperApiService extends BaseApiService{
    private final HelperApiInterface apiInterface;

    public HelperApiService() {
        this.apiInterface = RetrofitClient.getHelperApiService();
    }



    /**
     * Get helper profile by ID
     */
    public CompletableFuture<HelperResponse> getHelperProfile(Context context, int helperId) {
        return executeCall(context, apiInterface.getHelperProfile(helperId), "Get helper profile by ID");
    }

    public void getHelperProfile(Context context, int helperId, BaseApiService.ApiCallback<HelperResponse> callback) {
        handleApiResponse(context, getHelperProfile(context, helperId), callback);
    }

    /**
     * Update helper profile
     */
    public CompletableFuture<HelperResponse> updateHelperProfile(Context context, HelperUpdateRequest request) {
        return executeCall(context, apiInterface.updateHelperProfile(request), "Update helper profile");
    }

    public void updateHelperProfile(Context context, HelperUpdateRequest request, BaseApiService.ApiCallback<HelperResponse> callback) {
        handleApiResponse(context, updateHelperProfile(context, request), callback);
    }

    /**
     * Update helper profile by ID (admin function)
     */
    public CompletableFuture<HelperResponse> updateHelperProfileById(Context context, int helperId, HelperUpdateRequest request) {
        return executeCall(context, apiInterface.updateHelperProfileById(helperId, request), "Update helper profile by ID");
    }

    public void updateHelperProfileById(Context context, int helperId, HelperUpdateRequest request, BaseApiService.ApiCallback<HelperResponse> callback) {
        handleApiResponse(context, updateHelperProfileById(context, helperId, request), callback);
    }
}
