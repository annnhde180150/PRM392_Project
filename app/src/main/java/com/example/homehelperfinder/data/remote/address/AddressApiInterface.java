package com.example.homehelperfinder.data.remote.address;

import com.example.homehelperfinder.data.model.request.UserAddressUpdateRequest;
import com.example.homehelperfinder.data.model.response.ApiResponse;
import com.example.homehelperfinder.data.model.response.UserAddressResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface AddressApiInterface {
    @PUT("Address/UserAddress/{userId}")
    Call<ApiResponse<UserAddressResponse>> updateUserAddress(@Path("userId") int userId, @Body UserAddressUpdateRequest request);
}
