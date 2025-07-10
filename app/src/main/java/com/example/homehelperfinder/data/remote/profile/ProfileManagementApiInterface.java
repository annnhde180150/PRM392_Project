package com.example.homehelperfinder.data.remote.profile;

import com.example.homehelperfinder.data.model.response.ApiResponse;
import com.example.homehelperfinder.data.model.request.BanUnbanRequest;
import com.example.homehelperfinder.data.model.response.ProfileResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ProfileManagementApiInterface {
    @GET("ProfileManagement/banned")
    Call<ApiResponse<List<ProfileResponse>>> getBannedProfiles();

    @GET("ProfileManagement/active")
    Call<ApiResponse<List<ProfileResponse>>> getActiveProfiles();

    @POST("ProfileManagement/ban")
    Call<ApiResponse<ProfileResponse>> banProfile(@Body BanUnbanRequest request);

    @POST("ProfileManagement/unban")
    Call<ApiResponse<ProfileResponse>> unbanProfile(@Body BanUnbanRequest request);

    @POST("ProfileManagement/bulk-ban")
    Call<ApiResponse<List<ProfileResponse>>> bulkBanProfiles(@Body List<BanUnbanRequest> request);

    @POST("ProfileManagement/bulk-unban")
    Call<ApiResponse<List<ProfileResponse>>> bulkUnbanProfiles(@Body List<BanUnbanRequest> request);
}