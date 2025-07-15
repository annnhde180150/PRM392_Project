package com.example.homehelperfinder.data.remote.address;

import android.content.Context;

import com.example.homehelperfinder.data.model.request.UserAddressCreateRequest;
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

    public CompletableFuture<UserAddressResponse> getUserAddress(Context context, int addressId) {
        return executeCall(context, apiInterface.getUserAddress(addressId), "Get user address by ID");
    }
    public void getUserAddress(Context context, int addressId, BaseApiService.ApiCallback<UserAddressResponse> callback) {
        handleApiResponse(context, getUserAddress(context, addressId), callback);
    }

    public CompletableFuture<UserAddressResponse> updateUserAddress(Context context, int addressId, UserAddressUpdateRequest request) {
        return executeCall(context, apiInterface.updateUserAddress(addressId, request), "Update user address");
    }
    public void updateUserAddress(Context context, int addressId, UserAddressUpdateRequest request, BaseApiService.ApiCallback<UserAddressResponse> callback) {
        handleApiResponse(context, updateUserAddress(context, addressId, request), callback);
    }


    public CompletableFuture<Void> deleteUserAddress(Context context, int addressId) {
        return executeCall(context, apiInterface.deleteUserAddress(addressId), "Delete address by ID");
    }
    public void deleteUserAddress(Context context, int addressId, BaseApiService.ApiCallback<Void> callback) {
        handleApiResponse(context, deleteUserAddress(context, addressId), callback);
    }


    public CompletableFuture<Void> createUserAddress(Context context, UserAddressCreateRequest request) {
        return executeCall(context, apiInterface.createUserAddress(request), "Create new address");
    }
    public void createUserAddress(Context context, UserAddressCreateRequest request, BaseApiService.ApiCallback<Void> callback) {
        handleApiResponse(context, createUserAddress(context, request), callback);
    }


    public CompletableFuture<List<UserAddressResponse>> getUserAddressesByUserId(Context context, int userId) {
        return executeCall(context, apiInterface.getUserAddressesByUserId(userId), "Get all addresses by userId");
    }
    public void getUserAddressesByUserId(Context context, int userId, BaseApiService.ApiCallback<List<UserAddressResponse>> callback) {
        handleApiResponse(context, getUserAddressesByUserId(context, userId), callback);
    public void updateUserAddress(Context context, int userId, UserAddressUpdateRequest request, BaseApiService.ApiCallback<UserAddressResponse> callback) {
        handleApiResponse(context, updateUserAddress(context, userId, request), callback);
    }

    public CompletableFuture<List<UserAddressResponse>> getUserAddresses(Context context, int userId) {
        return executeCall(context, apiInterface.getUserAddresses(userId), "Get Addresses by userId");
    }
    public void getUserAddresses(Context context, int userId, BaseApiService.ApiCallback<List<UserAddressResponse>> callback) {
        handleApiResponse(context, getUserAddresses(context, userId), callback);
    }
}
