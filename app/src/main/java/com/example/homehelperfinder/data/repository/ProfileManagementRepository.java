package com.example.homehelperfinder.data.repository;

import android.content.Context;

import com.example.homehelperfinder.data.model.request.BanUnbanRequest;
import com.example.homehelperfinder.data.model.response.ProfileResponse;
import com.example.homehelperfinder.data.remote.BaseApiService;
import com.example.homehelperfinder.data.remote.profile.ProfileManagementApiService;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ProfileManagementRepository {
    private final ProfileManagementApiService apiService;

    public ProfileManagementRepository() {
        this.apiService = new ProfileManagementApiService();
    }

    // CompletableFuture methods
    public CompletableFuture<List<ProfileResponse>> getBannedProfiles(Context context) {
        return apiService.getBannedProfiles(context);
    }

    public CompletableFuture<List<ProfileResponse>> getActiveProfiles(Context context) {
        return apiService.getActiveProfiles(context);
    }

    // Callback methods for easier usage
    public void getBannedProfiles(Context context, BaseApiService.ApiCallback<List<ProfileResponse>> callback) {
        apiService.getBannedProfiles(context, callback);
    }

    public void getActiveProfiles(Context context, BaseApiService.ApiCallback<List<ProfileResponse>> callback) {
        apiService.getActiveProfiles(context, callback);
    }

    // Ban/Unban methods
    public CompletableFuture<ProfileResponse> banProfile(Context context, BanUnbanRequest request) {
        return apiService.banProfile(context, request);
    }

    public CompletableFuture<ProfileResponse> unbanProfile(Context context, BanUnbanRequest request) {
        return apiService.unbanProfile(context, request);
    }

    public void banProfile(Context context, BanUnbanRequest request, BaseApiService.ApiCallback<ProfileResponse> callback) {
        apiService.banProfile(context, request, callback);
    }

    public void unbanProfile(Context context, BanUnbanRequest request, BaseApiService.ApiCallback<ProfileResponse> callback) {
        apiService.unbanProfile(context, request, callback);
    }

    // Bulk Ban/Unban methods
    public CompletableFuture<List<ProfileResponse>> bulkBanProfiles(Context context, List<BanUnbanRequest> request) {
        return apiService.bulkBanProfiles(context, request);
    }

    public CompletableFuture<List<ProfileResponse>> bulkUnbanProfiles(Context context, List<BanUnbanRequest> request) {
        return apiService.bulkUnbanProfiles(context, request);
    }

    public void bulkBanProfiles(Context context, List<BanUnbanRequest> request, BaseApiService.ApiCallback<List<ProfileResponse>> callback) {
        apiService.bulkBanProfiles(context, request, callback);
    }

    public void bulkUnbanProfiles(Context context, List<BanUnbanRequest> request, BaseApiService.ApiCallback<List<ProfileResponse>> callback) {
        apiService.bulkUnbanProfiles(context, request, callback);
    }
}