package com.example.homehelperfinder.data.repository;

import android.content.Context;
import com.example.homehelperfinder.data.model.ProfileModel;
import com.example.homehelperfinder.data.remote.BaseApiService;
import com.example.homehelperfinder.data.remote.ProfileApiService;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ProfileRepository {
    private final ProfileApiService apiService;

    public ProfileRepository() {
        this.apiService = new ProfileApiService();
    }

    // CompletableFuture methods
    public CompletableFuture<List<ProfileModel>> getBannedProfiles(Context context) {
        return apiService.getBannedProfiles(context);
    }

    public CompletableFuture<List<ProfileModel>> getActiveProfiles(Context context) {
        return apiService.getActiveProfiles(context);
    }

    // Callback methods for easier usage
    public void getBannedProfiles(Context context, BaseApiService.ApiCallback<List<ProfileModel>> callback) {
        apiService.getBannedProfiles(context, callback);
    }

    public void getActiveProfiles(Context context, BaseApiService.ApiCallback<List<ProfileModel>> callback) {
        apiService.getActiveProfiles(context, callback);
    }
} 