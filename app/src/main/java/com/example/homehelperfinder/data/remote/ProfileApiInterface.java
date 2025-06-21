package com.example.homehelperfinder.data.remote;

import com.example.homehelperfinder.data.model.ApiResponse;
import com.example.homehelperfinder.data.model.ProfileModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Retrofit interface for ProfileManagement API endpoints
 */
public interface ProfileApiInterface {

    /**
     * Get all banned profiles
     * @return Call containing API response with list of banned profiles
     */
    @GET("banned")
    Call<ApiResponse<List<ProfileModel>>> getBannedProfiles();

    /**
     * Get all active profiles
     * @return Call containing API response with list of active profiles
     */
    @GET("active")
    Call<ApiResponse<List<ProfileModel>>> getActiveProfiles();
} 