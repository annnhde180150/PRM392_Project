package com.example.homehelperfinder.data.remote.user;

import com.example.homehelperfinder.data.model.request.ChangePasswordRequest;
import com.example.homehelperfinder.data.model.request.UserAddressUpdateRequest;
import com.example.homehelperfinder.data.model.request.UserUpdateRequest;
import com.example.homehelperfinder.data.model.response.ApiResponse;
import com.example.homehelperfinder.data.model.response.UserAddressResponse;
import com.example.homehelperfinder.data.model.response.UserResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserApiInterface {

    @PUT("User/change-password/{userId}")
    Call<ApiResponse<String>> changePassword(@Path("userId") int userId, @Body ChangePasswordRequest request);
}
