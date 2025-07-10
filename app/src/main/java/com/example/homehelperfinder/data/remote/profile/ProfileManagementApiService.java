package com.example.homehelperfinder.data.remote.profile;

import android.content.Context;

import com.example.homehelperfinder.data.model.request.BanUnbanRequest;
import com.example.homehelperfinder.data.model.response.ProfileResponse;
import com.example.homehelperfinder.data.remote.BaseApiService;
import com.example.homehelperfinder.data.remote.RetrofitClient;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ProfileManagementApiService extends BaseApiService {
    private final ProfileManagementApiInterface apiInterface;

    public ProfileManagementApiService() {
        super();
        this.apiInterface = RetrofitClient.getProfileApiService();
    }

    public CompletableFuture<List<ProfileResponse>> getBannedProfiles(Context context) {
        return executeCall(context, apiInterface.getBannedProfiles(), "getBannedProfiles");
    }

    public CompletableFuture<List<ProfileResponse>> getActiveProfiles(Context context) {
        return executeCall(context, apiInterface.getActiveProfiles(), "getActiveProfiles");
    }

    public void getBannedProfiles(Context context, ApiCallback<List<ProfileResponse>> callback) {
        handleApiResponse(context, getBannedProfiles(context), callback);
    }

    public void getActiveProfiles(Context context, ApiCallback<List<ProfileResponse>> callback) {
        handleApiResponse(context, getActiveProfiles(context), callback);
    }

    public CompletableFuture<ProfileResponse> banProfile(Context context, BanUnbanRequest request) {
        return executeCall(context, apiInterface.banProfile(request), "banProfile");
    }

    public CompletableFuture<ProfileResponse> unbanProfile(Context context, BanUnbanRequest request) {
        return executeCall(context, apiInterface.unbanProfile(request), "unbanProfile");
    }

    public void banProfile(Context context, BanUnbanRequest request, ApiCallback<ProfileResponse> callback) {
        handleApiResponse(context, banProfile(context, request), callback);
    }

    public void unbanProfile(Context context, BanUnbanRequest request, ApiCallback<ProfileResponse> callback) {
        handleApiResponse(context, unbanProfile(context, request), callback);
    }

    // Bulk Ban/Unban methods
    public CompletableFuture<List<ProfileResponse>> bulkBanProfiles(Context context, List<BanUnbanRequest> request) {
        return executeCall(context, apiInterface.bulkBanProfiles(request), "bulkBanProfiles");
    }

    public CompletableFuture<List<ProfileResponse>> bulkUnbanProfiles(Context context, List<BanUnbanRequest> request) {
        return executeCall(context, apiInterface.bulkUnbanProfiles(request), "bulkUnbanProfiles");
    }

    public void bulkBanProfiles(Context context, List<BanUnbanRequest> request, ApiCallback<List<ProfileResponse>> callback) {
        handleApiResponse(context, bulkBanProfiles(context, request), callback);
    }

    public void bulkUnbanProfiles(Context context, List<BanUnbanRequest> request, ApiCallback<List<ProfileResponse>> callback) {
        handleApiResponse(context, bulkUnbanProfiles(context, request), callback);
    }
}
