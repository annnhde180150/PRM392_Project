package com.example.homehelperfinder.data.remote.booking

import android.content.Context
import com.example.homehelperfinder.data.model.request.AcceptRequestRequest
import com.example.homehelperfinder.data.model.request.BookingCancelRequest
import com.example.homehelperfinder.data.model.request.BookingCreateRequest
import com.example.homehelperfinder.data.model.request.BookingUpdateRequest
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

    fun getBooking(context : Context, id : Int) : CompletableFuture<BookingDetailResponse>{
        return executeCall(context, api.getBooking(id), "get Booking")
    }

    fun getBooking(context : Context, id : Int, callback : ApiCallback<BookingDetailResponse>){
        handleApiResponse(context, getBooking(context, id), callback)
    }

    fun updateBooking(context : Context, request : BookingUpdateRequest) : CompletableFuture<BookingDetailResponse>{
        return executeCall(context, api.updateBooking(request), "update Booking")
    }

    fun updateBooking(context : Context, request : BookingUpdateRequest, callback : ApiCallback<BookingDetailResponse>){
        handleApiResponse(context, updateBooking(context, request), callback)
    }

    fun cancelBooking(context : Context, request : BookingCancelRequest) : CompletableFuture<String>{
        return executeCall(context, api.cancelBooking(request), "cancel Booking")
    }

    fun cancelBooking(context : Context, request : BookingCancelRequest, callback : ApiCallback<String>){
        handleApiResponse(context, cancelBooking(context, request), callback)
    }

    fun getBookingByUser(context : Context, id : Int) : CompletableFuture<List<BookingDetailResponse>>{
        return executeCall(context, api.getPendingByUserId(id), "get Bookings")
    }

    fun getBookingByUser(context : Context, id : Int, callback : ApiCallback<List<BookingDetailResponse>>){
        handleApiResponse(context, getBookingByUser(context, id), callback)
    }

    fun getUserSchedule(context : Context, id : Int) : CompletableFuture<List<BookingDetailResponse>>{
        return executeCall(context, api.getUserSchedule(id), "get user schedule")
    }

    fun getUserSchedule(context : Context, id : Int, callback : ApiCallback<List<BookingDetailResponse>>){
        handleApiResponse(context, getUserSchedule(context, id), callback)
    }

    fun getHelperSchedule(context : Context, id : Int) : CompletableFuture<List<BookingDetailResponse>>{
        return executeCall(context, api.getHelperSchedule(id), "get user schedule")
    }

    fun getHelperSchedule(context : Context, id : Int, callback : ApiCallback<List<BookingDetailResponse>>){
        handleApiResponse(context, getHelperSchedule(context, id), callback)
    }
}