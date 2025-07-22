package com.example.homehelperfinder.data.remote.serviceRequest

import android.content.Context
import com.example.homehelperfinder.data.model.request.NewRequestRequest
import com.example.homehelperfinder.data.model.request.UpdateRequestRequest
import com.example.homehelperfinder.data.model.response.ApiResponse
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

    fun getRequest(context : Context, id : Int) : CompletableFuture<RequestDetailResponse>{
        return executeCall(context, apiInterface.getRequest(id), "GetRequest")
    }

    fun getRequest(context : Context, id : Int, callback : ApiCallback<RequestDetailResponse>){
        handleApiResponse(context, getRequest(context, id), callback)
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

    fun deleteRequest(context : Context, id : Int) : CompletableFuture<String>{
        return executeCall(context, apiInterface.deleteRequest(id), "DeleteRequest")
    }

    fun deleteRequest(context : Context, id : Int, callback : ApiCallback<String>) {
        handleApiResponse(context, deleteRequest(context, id), callback)
    }

    fun getAvailableRequests(context : Context) : CompletableFuture<List<RequestDetailResponse>>{
        return executeCall(context, apiInterface.getAvailableRequest(), "GetAvailableRequests")
    }

    fun getAvailableRequests(context : Context, callback : ApiCallback<List<RequestDetailResponse>>) {
        handleApiResponse(context, getAvailableRequests(context), callback)
    }

    fun getUserPending(context : Context, id : Int) : CompletableFuture<List<RequestDetailResponse>>{
        return executeCall(context, apiInterface.getUserUnbookedRequest(id), "GetPendingRequests")
    }

    fun getUserPending(context : Context, id : Int, callback : ApiCallback<List<RequestDetailResponse>>) {
        handleApiResponse(context, getUserPending(context, id), callback)
    }

}