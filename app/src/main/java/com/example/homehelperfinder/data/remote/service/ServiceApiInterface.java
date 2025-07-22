package com.example.homehelperfinder.data.remote.service;

import com.example.homehelperfinder.data.model.response.ApiResponse;
import com.example.homehelperfinder.data.model.response.ServiceResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ServiceApiInterface {
    @GET("Service/active")
    Call<ApiResponse<List<ServiceResponse>>> getActiveServices();

    @GET("Service/GetService/{id}")
    Call<ApiResponse<ServiceResponse>> getService(@Path("id") int Id);
}
