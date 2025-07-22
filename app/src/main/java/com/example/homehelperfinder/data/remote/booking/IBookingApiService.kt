package com.example.homehelperfinder.data.remote.booking

import com.example.homehelperfinder.data.model.request.AcceptRequestRequest
import com.example.homehelperfinder.data.model.request.BookingCreateRequest
import com.example.homehelperfinder.data.model.request.NewRequestRequest
import com.example.homehelperfinder.data.model.response.ApiResponse
import com.example.homehelperfinder.data.model.response.BookingDetailResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface IBookingApiService {
    @POST("Bookings/CreateBooking")
    fun createBooking(@Body request : BookingCreateRequest) : Call<ApiResponse<BookingDetailResponse>>

    @POST("Bookings/BookHelper/{id}")
    fun bookHelper(@Body request  : NewRequestRequest, @Path("id") helperId : Int) : Call<ApiResponse<BookingDetailResponse>>

    @POST("Bookings/AcceptRequest")
    fun acceptRequest(@Body request : AcceptRequestRequest) : Call<ApiResponse<BookingDetailResponse>>
}