package com.example.homehelperfinder.data.remote.user;

import android.content.Context;

import com.example.homehelperfinder.data.model.request.UserAddressUpdateRequest;
import com.example.homehelperfinder.data.model.request.UserUpdateRequest;
import com.example.homehelperfinder.data.model.response.ApiResponse;
import com.example.homehelperfinder.data.model.response.ServiceResponse;
import com.example.homehelperfinder.data.model.response.UserAddressResponse;
import com.example.homehelperfinder.data.model.response.UserResponse;
import com.example.homehelperfinder.data.remote.BaseApiService;
import com.example.homehelperfinder.data.remote.RetrofitClient;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserApiService extends BaseApiService {
    
    private final UserApiInterface apiInterface;
    
    public UserApiService() {
        this.apiInterface = RetrofitClient.getUserApiService();
    }
    

    
    /**
     * Get user profile by ID
     */

    public CompletableFuture<UserResponse> getUserProfile(Context context, int userId) {
        return executeCall(context, apiInterface.getUserProfile(userId), "Get user profile by ID");
    }

    public void getUserProfile(Context context, int userId, ApiCallback<UserResponse> callback) {
        handleApiResponse(context, getUserProfile(context, userId), callback);
    }


    
    /**
     * Update user profile by ID
     */
    public CompletableFuture<UserResponse> updateUserProfile(Context context, int userId, UserUpdateRequest request) {
        return executeCall(context, apiInterface.updateUserProfile(userId, request), "Update user profile by ID");
    }


    public void updateUserProfile(Context context, int userId, UserUpdateRequest request, ApiCallback<UserResponse> callback) {
        handleApiResponse(context, updateUserProfile(context, userId, request), callback);
    }



}
