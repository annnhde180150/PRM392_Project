package com.example.homehelperfinder.data.remote.helper;

import com.example.homehelperfinder.data.model.request.HelperUpdateRequest;
import com.example.homehelperfinder.data.model.response.ApiResponse;
import com.example.homehelperfinder.data.model.response.HelperResponse;
import com.example.homehelperfinder.data.model.response.HelperViewIncomeResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface HelperApiInterface {/**
 * Get helper profile by ID
 */
@GET("helper/profile/{helperId}")
Call<ApiResponse<HelperResponse>> getHelperProfile(@Path("helperId") int helperId);



    /**
     * Update helper profile
     */
    @PUT("helper/profile")
    Call<ApiResponse<HelperResponse>> updateHelperProfile(@Body HelperUpdateRequest request);

    /**
     * Update helper profile by ID (admin function)
     */
    @PUT("helper/profile/{helperId}")
    Call<ApiResponse<HelperResponse>> updateHelperProfileById(@Path("helperId") int helperId, @Body HelperUpdateRequest request);

    /**
     * View helper income by ID
     */
    @GET("Helper/ViewIncome/{helperId}")
    Call<ApiResponse<HelperViewIncomeResponse>> viewHelperIncome(@Path("helperId") int helperId);
}
