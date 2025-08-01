package com.example.homehelperfinder.data.remote.user;

import android.content.Context;

import com.example.homehelperfinder.data.model.request.ChangePasswordRequest;
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


    public CompletableFuture<String> changePassword(Context context, int userId, ChangePasswordRequest request) {
        return executeCall(context, apiInterface.changePassword(userId, request), "changePassword");
    }

    public void changePassword(Context context, int userId, ChangePasswordRequest request, ApiCallback<String> callback) {
        handleApiResponse(context, changePassword(context, userId, request), callback);
    }


}
