package com.example.homehelperfinder.data.remote.serviceRequest

import android.content.Context
import com.example.homehelperfinder.data.model.request.NewRequestRequest
import com.example.homehelperfinder.data.model.request.UpdateRequestRequest
import com.example.homehelperfinder.data.model.response.RequestDetailResponse
import com.example.homehelperfinder.data.remote.BaseApiService
import com.example.homehelperfinder.data.remote.RetrofitClient
import retrofit2.Call
import java.util.concurrent.CompletableFuture

class ServiceRequestApiService(context : Context) : BaseApiService() {
    private val context = context.applicationContext
    private lateinit var apiInterface: ServiceRequestApiInterface

    init {
        RetrofitClient.init(this.context)
        apiInterface = RetrofitClient.getServicerRequestApiService()
    }

    fun createRequest(context : Context, request : NewRequestRequest) : CompletableFuture<RequestDetailResponse>{
        return executeCall(context, apiInterface.createRequest(request), "CreateRequest")
    }

    fun createRequest(context : Context, request : NewRequestRequest, callback : ApiCallback<RequestDetailResponse>){
        handleApiResponse(context, createRequest(context, request), callback)
    }

    fun updateRequest(context : Context, request : UpdateRequestRequest) : CompletableFuture<RequestDetailResponse>{
        return executeCall(context, apiInterface.updateRequest(request), "CreateRequest")
    }

    fun updateRequest(context : Context, request : UpdateRequestRequest, callback : ApiCallback<RequestDetailResponse>){
        handleApiResponse(context, updateRequest(context, request), callback)
    }

}