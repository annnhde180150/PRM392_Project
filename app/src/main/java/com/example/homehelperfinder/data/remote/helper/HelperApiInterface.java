package com.example.homehelperfinder.data.remote.helper;

import com.example.homehelperfinder.data.model.request.HelperUpdateRequest;
import com.example.homehelperfinder.data.model.response.ApiResponse;
import com.example.homehelperfinder.data.model.response.HelperResponse;
import com.example.homehelperfinder.data.model.response.HelperSearchResponse;
import com.example.homehelperfinder.data.model.response.HelperViewIncomeResponse;
import com.example.homehelperfinder.data.model.response.ServiceResponse;

import java.util.List;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface HelperApiInterface {

    /**
     * Search helpers by service
     */
    @GET("Helper/search")
    Call<ApiResponse<List<HelperSearchResponse>>> searchHelpers(
            @Query("serviceId") int serviceId,
            @Query("page") int page,
            @Query("pageSize") int pageSize
    );

    /**
     * View helper income by ID
     */
    @GET("Helper/ViewIncome/{helperId}")
    Call<ApiResponse<HelperViewIncomeResponse>> viewHelperIncome(@Path("helperId") int helperId);

    @GET("Helper/GetHelperServices/{id}")
    Call<ApiResponse<List<ServiceResponse>>>  getHelperService(@Path("id") int helperId);
}
