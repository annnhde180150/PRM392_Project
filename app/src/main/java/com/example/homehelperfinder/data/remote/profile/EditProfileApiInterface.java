package com.example.homehelperfinder.data.remote.profile;

import com.example.homehelperfinder.data.model.request.HelperUpdateRequest;
import com.example.homehelperfinder.data.model.request.UserUpdateRequest;
import com.example.homehelperfinder.data.model.response.ApiResponse;
import com.example.homehelperfinder.data.model.response.HelperResponse;
import com.example.homehelperfinder.data.model.response.UserResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface EditProfileApiInterface {

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


    /**
     * Get helper profile by ID
     */

    @GET("helper/profile/{helperId}")
    Call<ApiResponse<HelperResponse>> getHelperProfile(@Path("helperId") int helperId);

    @PUT("helper/profile/{helperId}")
    Call<ApiResponse<HelperResponse>> updateHelperProfile(@Path("helperId") int helperId, @Body HelperUpdateRequest request);

    /**
     * Update helper profile by ID (admin function)
     */
    @PUT("helper/profile/{helperId}")
    Call<ApiResponse<HelperResponse>> updateHelperProfileById(@Path("helperId") int helperId, @Body HelperUpdateRequest request);
}
