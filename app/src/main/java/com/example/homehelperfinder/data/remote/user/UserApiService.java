package com.example.homehelperfinder.data.remote.user;

import android.content.Context;

import com.example.homehelperfinder.data.model.request.UserUpdateRequest;
import com.example.homehelperfinder.data.model.response.UserResponse;
import com.example.homehelperfinder.data.remote.BaseApiService;
import com.example.homehelperfinder.data.remote.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserApiService extends BaseApiService {
    
    private final UserApiInterface apiInterface;
    
    public UserApiService() {
        this.apiInterface = RetrofitClient.getClient().create(UserApiInterface.class);
    }
    

    
    /**
     * Get user profile by ID
     */
    public void getUserProfile(Context context, int userId, ApiCallback<UserResponse> callback) {
        Call<UserResponse> call = apiInterface.getUserProfile(userId);
        handleResponse(context, call, "Get user profile by ID", callback);
    }
    
    /**
     * Update user profile
     */
    public void updateUserProfile(Context context, UserUpdateRequest request, ApiCallback<UserResponse> callback) {
        Call<UserResponse> call = apiInterface.updateUserProfile(request);
        handleResponse(context, call, "Update user profile", callback);
    }
    
    /**
     * Update user profile by ID (admin function)
     */
    public void updateUserProfileById(Context context, int userId, UserUpdateRequest request, ApiCallback<UserResponse> callback) {
        Call<UserResponse> call = apiInterface.updateUserProfileById(userId, request);
        handleResponse(context, call, "Update user profile by ID", callback);
    }
}
