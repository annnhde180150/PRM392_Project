package com.example.homehelperfinder.data.remote.authentication;

import com.example.homehelperfinder.data.model.ApiResponse;
import com.example.homehelperfinder.data.model.request.BanUnbanRequest;
import com.example.homehelperfinder.data.model.request.HelperRequest;
import com.example.homehelperfinder.data.model.response.HelperResponse;
import com.example.homehelperfinder.data.model.response.ProfileResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthenticationApiInterface {

    @POST("Authentication/register/helper")
    Call<ApiResponse<HelperResponse>> registerHelper(@Body HelperRequest request);

}
