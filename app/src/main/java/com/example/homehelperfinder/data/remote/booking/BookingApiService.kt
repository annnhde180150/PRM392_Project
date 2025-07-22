package com.example.homehelperfinder.data.remote.booking

import android.content.Context
import com.example.homehelperfinder.data.model.request.AcceptRequestRequest
import com.example.homehelperfinder.data.model.request.BookingCreateRequest
import com.example.homehelperfinder.data.model.request.NewRequestRequest
import com.example.homehelperfinder.data.model.response.BookingDetailResponse
import com.example.homehelperfinder.data.model.response.RequestDetailResponse
import com.example.homehelperfinder.data.model.response.ServiceResponse
import com.example.homehelperfinder.data.remote.BaseApiService
import com.example.homehelperfinder.data.remote.RetrofitClient
import java.util.concurrent.CompletableFuture

class BookingApiService : BaseApiService() {
    private lateinit var api : IBookingApiService

    init {
        api = RetrofitClient.getBookingApiService()
    }

    fun createBooking(context : Context, request : BookingCreateRequest) : CompletableFuture<BookingDetailResponse>{
        return executeCall(context, api.createBooking(request), "Create Booking")
    }

    fun createBooking(context : Context, request : BookingCreateRequest, callback : ApiCallback<BookingDetailResponse>){
        handleApiResponse(context, createBooking(context, request), callback)
    }

    fun bookHelper(context : Context, request : NewRequestRequest, helperId : Int) : CompletableFuture<BookingDetailResponse>{
        return executeCall(context, api.bookHelper(request, helperId), "Book Helper")
    }

    fun bookHelper(context : Context, request : NewRequestRequest, helperId : Int, callback : ApiCallback<BookingDetailResponse>){
        handleApiResponse(context, bookHelper(context, request, helperId), callback)
    }

    fun acceptRequest(context : Context, request : AcceptRequestRequest) : CompletableFuture<BookingDetailResponse>{
        return executeCall(context, api.acceptRequest(request), "Accept Request")
    }

    fun acceptRequest(context : Context, request : AcceptRequestRequest, callback : ApiCallback<BookingDetailResponse>){
        handleApiResponse(context, acceptRequest(context, request), callback)
    }
}