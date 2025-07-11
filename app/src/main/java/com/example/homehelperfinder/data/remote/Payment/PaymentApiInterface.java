package com.example.homehelperfinder.data.remote.Payment;

import com.example.homehelperfinder.data.model.response.ApiResponse;
import com.example.homehelperfinder.data.model.response.GetPaymentResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
public interface PaymentApiInterface {
    @GET("Payment/GetPayment")
    Call<ApiResponse<GetPaymentResponse>> getPaymentDetails(
            @Query("userId") int userId,
            @Query("bookingId") int bookingId
    );
}
