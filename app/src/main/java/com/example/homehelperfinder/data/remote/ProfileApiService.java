package com.example.homehelperfinder.data.remote;

import android.content.Context;

import com.example.homehelperfinder.data.model.BanUnbanRequest;
import com.example.homehelperfinder.data.model.ProfileModel;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ProfileApiService extends BaseApiService {
    private final ProfileApiInterface apiInterface;

    public ProfileApiService() {
        super();
        this.apiInterface = RetrofitClient.getProfileApiService();
    }

    public CompletableFuture<List<ProfileModel>> getBannedProfiles(Context context) {
        return executeCall(context, apiInterface.getBannedProfiles(), "getBannedProfiles");
    }

    public CompletableFuture<List<ProfileModel>> getActiveProfiles(Context context) {
        return executeCall(context, apiInterface.getActiveProfiles(), "getActiveProfiles");
    }

    public void getBannedProfiles(Context context, ApiCallback<List<ProfileModel>> callback) {
        handleApiResponse(context, getBannedProfiles(context), callback);
    }

    public void getActiveProfiles(Context context, ApiCallback<List<ProfileModel>> callback) {
        handleApiResponse(context, getActiveProfiles(context), callback);
    }

    public CompletableFuture<ProfileModel> banProfile(Context context, BanUnbanRequest request) {
        return executeCall(context, apiInterface.banProfile(request), "banProfile");
    }

    public CompletableFuture<ProfileModel> unbanProfile(Context context, BanUnbanRequest request) {
        return executeCall(context, apiInterface.unbanProfile(request), "unbanProfile");
    }

    public void banProfile(Context context, BanUnbanRequest request, ApiCallback<ProfileModel> callback) {
        handleApiResponse(context, banProfile(context, request), callback);
    }

    public void unbanProfile(Context context, BanUnbanRequest request, ApiCallback<ProfileModel> callback) {
        handleApiResponse(context, unbanProfile(context, request), callback);
    }

    // Bulk Ban/Unban methods
    public CompletableFuture<List<ProfileModel>> bulkBanProfiles(Context context, List<BanUnbanRequest> request) {
        return executeCall(context, apiInterface.bulkBanProfiles(request), "bulkBanProfiles");
    }

    public CompletableFuture<List<ProfileModel>> bulkUnbanProfiles(Context context, List<BanUnbanRequest> request) {
        return executeCall(context, apiInterface.bulkUnbanProfiles(request), "bulkUnbanProfiles");
    }

    public void bulkBanProfiles(Context context, List<BanUnbanRequest> request, ApiCallback<List<ProfileModel>> callback) {
        handleApiResponse(context, bulkBanProfiles(context, request), callback);
    }

    public void bulkUnbanProfiles(Context context, List<BanUnbanRequest> request, ApiCallback<List<ProfileModel>> callback) {
        handleApiResponse(context, bulkUnbanProfiles(context, request), callback);
    }
}
