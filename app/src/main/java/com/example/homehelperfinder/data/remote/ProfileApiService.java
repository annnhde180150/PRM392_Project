package com.example.homehelperfinder.data.remote;

import android.util.Log;

import com.example.homehelperfinder.data.model.ApiResponse;
import com.example.homehelperfinder.data.model.ProfileModel;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * API service for profile management operations using Retrofit
 */
public class ProfileApiService {
    private static final String TAG = "ProfileApiService";
    private final ProfileApiInterface apiInterface;

    public ProfileApiService() {
        this.apiInterface = RetrofitClient.getProfileApiService();
    }

    /**
     * Get all banned profiles using Retrofit
     * @return CompletableFuture containing list of banned profiles
     */
    public CompletableFuture<List<ProfileModel>> getBannedProfiles() {
        CompletableFuture<List<ProfileModel>> future = new CompletableFuture<>();
        
        Call<ApiResponse<List<ProfileModel>>> call = apiInterface.getBannedProfiles();
        
        call.enqueue(new Callback<ApiResponse<List<ProfileModel>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<ProfileModel>>> call, Response<ApiResponse<List<ProfileModel>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<ProfileModel>> apiResponse = response.body();
                    
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        List<ProfileModel> profiles = apiResponse.getData();
                        Log.d(TAG, "Successfully loaded " + profiles.size() + " banned profiles");
                        Log.d(TAG, "API Message: " + apiResponse.getMessage());
                        future.complete(profiles);
                    } else {
                        String errorMsg = "API Error - Success: " + apiResponse.isSuccess() + 
                                         ", Message: " + apiResponse.getMessage();
                        Log.e(TAG, errorMsg);
                        future.completeExceptionally(new Exception(errorMsg));
                    }
                } else {
                    String errorMsg = "Failed to fetch banned profiles. HTTP Code: " + response.code();
                    Log.e(TAG, errorMsg);
                    future.completeExceptionally(new Exception(errorMsg));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<ProfileModel>>> call, Throwable t) {
                Log.e(TAG, "Network error while fetching banned profiles", t);
                future.completeExceptionally(t);
            }
        });

        return future;
    }

    /**
     * Get all active profiles using Retrofit
     * @return CompletableFuture containing list of active profiles
     */
    public CompletableFuture<List<ProfileModel>> getActiveProfiles() {
        CompletableFuture<List<ProfileModel>> future = new CompletableFuture<>();
        
        Call<ApiResponse<List<ProfileModel>>> call = apiInterface.getActiveProfiles();
        
        call.enqueue(new Callback<ApiResponse<List<ProfileModel>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<ProfileModel>>> call, Response<ApiResponse<List<ProfileModel>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<ProfileModel>> apiResponse = response.body();
                    
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        List<ProfileModel> profiles = apiResponse.getData();
                        Log.d(TAG, "Successfully loaded " + profiles.size() + " active profiles");
                        Log.d(TAG, "API Message: " + apiResponse.getMessage());
                        future.complete(profiles);
                    } else {
                        String errorMsg = "API Error - Success: " + apiResponse.isSuccess() + 
                                         ", Message: " + apiResponse.getMessage();
                        Log.e(TAG, errorMsg);
                        future.completeExceptionally(new Exception(errorMsg));
                    }
                } else {
                    String errorMsg = "Failed to fetch active profiles. HTTP Code: " + response.code();
                    Log.e(TAG, errorMsg);
                    future.completeExceptionally(new Exception(errorMsg));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<ProfileModel>>> call, Throwable t) {
                Log.e(TAG, "Network error while fetching active profiles", t);
                future.completeExceptionally(t);
            }
        });

        return future;
    }
} 