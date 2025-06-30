package com.example.homehelperfinder.data.remote;

import com.example.homehelperfinder.data.model.ApiResponse;
import com.example.homehelperfinder.data.model.ProfileModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ProfileApiInterface {
    @GET("ProfileManagement/banned")
    Call<ApiResponse<List<ProfileModel>>> getBannedProfiles();

    @GET("ProfileManagement/active")
    Call<ApiResponse<List<ProfileModel>>> getActiveProfiles();
} 