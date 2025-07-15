package com.example.homehelperfinder.data.remote.user;

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
    
    /**
     * Get user profile by ID
     */
    @GET("user/profile/{userId}")
    Call<ApiResponse<UserResponse>> getUserProfile(@Path("userId") int userId);

    
    /**
     * Update user profile by ID
     */
    @PUT("user/profile/{userId}")
    Call<ApiResponse<UserResponse>> updateUserProfile(@Path("userId") int userId, @Body UserUpdateRequest request);


}
