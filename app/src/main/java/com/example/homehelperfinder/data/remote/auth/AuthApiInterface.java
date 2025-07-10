package com.example.homehelperfinder.data.remote.auth;

import com.example.homehelperfinder.data.model.request.AdminLoginRequest;
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
    Call<UserLoginResponse> loginUser(@Body LoginRequest request);
    
    @POST("Authentication/login/helper")
    Call<HelperLoginResponse> loginHelper(@Body LoginRequest request);
    
    @POST("Authentication/login/admin")
    Call<AdminLoginResponse> loginAdmin(@Body AdminLoginRequest request);
    
    @POST("Authentication/logout")
    Call<LogoutResponse> logout();
}
