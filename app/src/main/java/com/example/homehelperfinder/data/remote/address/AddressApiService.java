package com.example.homehelperfinder.data.remote.address;

import android.content.Context;

import com.example.homehelperfinder.data.model.request.UserAddressUpdateRequest;
import com.example.homehelperfinder.data.model.response.UserAddressResponse;
import com.example.homehelperfinder.data.remote.BaseApiService;
import com.example.homehelperfinder.data.remote.RetrofitClient;
import com.example.homehelperfinder.data.remote.user.UserApiInterface;

import java.util.concurrent.CompletableFuture;

public class AddressApiService extends BaseApiService{

    private final AddressApiInterface apiInterface;

    public AddressApiService() {
        this.apiInterface = RetrofitClient.getAddressApiInterface();
    }

    public CompletableFuture<UserAddressResponse> updateUserAddress(Context context, int userId, UserAddressUpdateRequest request) {
        return executeCall(context, apiInterface.updateUserAddress(userId, request), "Update user profile by ID");
    }


    public void updateUserAddress(Context context, int userId, UserAddressUpdateRequest request, BaseApiService.ApiCallback<UserAddressResponse> callback) {
        handleApiResponse(context, updateUserAddress(context, userId, request), callback);
    }
}
