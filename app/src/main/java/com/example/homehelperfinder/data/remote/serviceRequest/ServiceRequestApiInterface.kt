package com.example.homehelperfinder.data.remote.serviceRequest

import com.example.homehelperfinder.data.model.ApiResponse
import com.example.homehelperfinder.data.model.request.NewRequestRequest
import com.example.homehelperfinder.data.model.response.RequestDetailResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ServiceRequestApiInterface {
    @POST("ServiceRequests/CreateRequest")
    fun createRequest(@Body request : NewRequestRequest) : Call<ApiResponse<RequestDetailResponse>>
}