package com.example.homehelperfinder.data.remote.booking

import com.example.homehelperfinder.data.model.request.AcceptRequestRequest
import com.example.homehelperfinder.data.model.request.BookingCancelRequest
import com.example.homehelperfinder.data.model.request.BookingCreateRequest
import com.example.homehelperfinder.data.model.request.BookingUpdateRequest
import com.example.homehelperfinder.data.model.request.NewRequestRequest
import com.example.homehelperfinder.data.model.response.ApiResponse
import com.example.homehelperfinder.data.model.response.BookingDetailResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface IBookingApiService {
    @POST("Bookings/CreateBooking")
    fun createBooking(@Body request : BookingCreateRequest) : Call<ApiResponse<BookingDetailResponse>>

    @POST("Bookings/BookHelper/{id}")
    fun bookHelper(@Body request  : NewRequestRequest, @Path("id") helperId : Int) : Call<ApiResponse<BookingDetailResponse>>

    @POST("Bookings/AcceptRequest")
    fun acceptRequest(@Body request : AcceptRequestRequest) : Call<ApiResponse<BookingDetailResponse>>

    @GET("Bookings/getBooking/{id}")
    fun getBooking(@Path("id") id : Int) : Call<ApiResponse<BookingDetailResponse>>

    @PUT("Bookings/EditBookedRequest")
    fun updateBooking(@Body request : BookingUpdateRequest) : Call<ApiResponse<BookingDetailResponse>>

    @POST("Bookings/CancelBooking")
    fun cancelBooking(@Body request : BookingCancelRequest) : Call<ApiResponse<String>>

    @GET("Bookings/GetUserPendingBooking/{id}")
    fun getPendingByUserId(@Path("id") id : Int ) : Call<ApiResponse<List<BookingDetailResponse>>>

    @GET("Bookings/GetUserSchedule/{id}")
    fun getUserSchedule(@Path("id") id : Int) : Call<ApiResponse<List<BookingDetailResponse>>>

    @GET("Bookings/GetHelperSchedule/{id}")
    fun getHelperSchedule(@Path("id") id : Int) : Call<ApiResponse<List<BookingDetailResponse>>>
}