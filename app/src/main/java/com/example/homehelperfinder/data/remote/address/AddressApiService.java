package com.example.homehelperfinder.data.remote.address;

import android.content.Context;

import com.example.homehelperfinder.data.model.request.UserAddressUpdateRequest;
import com.example.homehelperfinder.data.model.response.UserAddressResponse;
import com.example.homehelperfinder.data.remote.BaseApiService;
import com.example.homehelperfinder.data.remote.RetrofitClient;
import com.example.homehelperfinder.data.remote.user.UserApiInterface;

import java.util.List;
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

    public CompletableFuture<List<UserAddressResponse>> getUserAddresses(Context context, int userId) {
        return executeCall(context, apiInterface.getUserAddresses(userId), "Get Addresses by userId");
    }
    public void getUserAddresses(Context context, int userId, BaseApiService.ApiCallback<List<UserAddressResponse>> callback) {
        handleApiResponse(context, getUserAddresses(context, userId), callback);
    }

    public CompletableFuture<UserAddressResponse> getAddress(Context context, int id) {
        return executeCall(context, apiInterface.getUserAddress(id), "Get Addresses by id");
    }

    public void getAddress(Context context, int id, BaseApiService.ApiCallback<UserAddressResponse> callback) {
        handleApiResponse(context, getAddress(context, id), callback);
    }
}
