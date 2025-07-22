package com.example.homehelperfinder.data.remote.serviceRequest

import com.example.homehelperfinder.data.model.request.NewRequestRequest
import com.example.homehelperfinder.data.model.request.UpdateRequestRequest
import com.example.homehelperfinder.data.model.response.ApiResponse
import com.example.homehelperfinder.data.model.response.GetAllServiceRequestResponse
import com.example.homehelperfinder.data.model.response.RequestDetailResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ServiceRequestApiInterface {
    @GET("ServiceRequests/GetRequest/{id}")
    fun getRequest(@Path("id") id : Int) : Call<ApiResponse<RequestDetailResponse>>

    @POST("ServiceRequests/CreateRequest")
    fun createRequest(@Body request : NewRequestRequest) : Call<ApiResponse<RequestDetailResponse>>

    @PUT("ServiceRequests/EditRequest")
    fun updateRequest(@Body request : UpdateRequestRequest) : Call<ApiResponse<RequestDetailResponse>>

    @DELETE("ServiceRequests/DeleteRequest/{id}")
    fun deleteRequest(@Path("id") id : Int) : Call<ApiResponse<String>>

    @GET("ServiceRequests/GetAvailableRequests")
    fun getAvailableRequest() : Call<ApiResponse<List<RequestDetailResponse>>>

    @GET("ServiceRequests/GetUserUnbookedRequest/{id}")
    fun getUserUnbookedRequest(@Path("id") id : Int) : Call<ApiResponse<List<RequestDetailResponse>>>
}