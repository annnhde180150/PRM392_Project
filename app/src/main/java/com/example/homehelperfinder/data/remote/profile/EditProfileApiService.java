package com.example.homehelperfinder.data.remote.profile;

import android.content.Context;

import com.example.homehelperfinder.data.model.request.HelperUpdateRequest;
import com.example.homehelperfinder.data.model.request.UserUpdateRequest;
import com.example.homehelperfinder.data.model.response.HelperResponse;
import com.example.homehelperfinder.data.model.response.UserResponse;
import com.example.homehelperfinder.data.remote.BaseApiService;
import com.example.homehelperfinder.data.remote.RetrofitClient;

import java.util.concurrent.CompletableFuture;

public class EditProfileApiService extends BaseApiService {
    private final EditProfileApiInterface apiInterface;

    public EditProfileApiService() {
        super();
        this.apiInterface = RetrofitClient.getEditProfileApiInterface();
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
    public CompletableFuture<HelperResponse> updateHelperProfile(Context context, int helperId, HelperUpdateRequest request) {
        return executeCall(context, apiInterface.updateHelperProfile(helperId, request), "Update helper profile");
    }

    public void updateHelperProfile(Context context,  int helperId, HelperUpdateRequest request, BaseApiService.ApiCallback<HelperResponse> callback) {
        handleApiResponse(context, updateHelperProfile(context, helperId, request), callback);
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
