package com.example.homehelperfinder.data.remote.auth;

import com.example.homehelperfinder.data.model.ApiResponse;
import com.example.homehelperfinder.data.model.request.LoginRequest;
import com.example.homehelperfinder.data.model.response.AdminLoginResponse;
import com.example.homehelperfinder.data.model.response.HelperLoginResponse;
import com.example.homehelperfinder.data.model.response.LogoutResponse;
import com.example.homehelperfinder.data.model.response.UserLoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Retrofit interface for authentication API endpoints
 */
public interface AuthApiInterface {

    @POST("Authentication/login/user")
    Call<ApiResponse<UserLoginResponse>> loginUser(@Body LoginRequest request);

    @POST("Authentication/login/helper")
    Call<ApiResponse<HelperLoginResponse>> loginHelper(@Body LoginRequest request);

    @POST("Authentication/login/admin")
    Call<ApiResponse<AdminLoginResponse>> loginAdmin(@Body LoginRequest request);

    @POST("Authentication/logout")
    Call<ApiResponse<LogoutResponse>> logout();
}
