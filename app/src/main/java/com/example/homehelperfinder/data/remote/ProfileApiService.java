package com.example.homehelperfinder.data.remote;

import android.content.Context;
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
}
