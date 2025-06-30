package com.example.homehelperfinder.data.remote;

import com.example.homehelperfinder.data.model.ApiResponse;
import com.example.homehelperfinder.data.model.BanUnbanRequest;
import com.example.homehelperfinder.data.model.ProfileModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ProfileApiInterface {
    @GET("ProfileManagement/banned")
    Call<ApiResponse<List<ProfileModel>>> getBannedProfiles();

    @GET("ProfileManagement/active")
    Call<ApiResponse<List<ProfileModel>>> getActiveProfiles();

    @POST("ProfileManagement/ban")
    Call<ApiResponse<ProfileModel>> banProfile(@Body BanUnbanRequest request);

    @POST("ProfileManagement/unban")
    Call<ApiResponse<ProfileModel>> unbanProfile(@Body BanUnbanRequest request);
}