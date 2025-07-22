package com.example.homehelperfinder.data.model.response;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetPaymentResponse {
    @SerializedName("bookingId")
    private int bookingId;

    @SerializedName("userId")
    private int userId;

    @SerializedName("amount")
    private int amount;

    @SerializedName("helperId")
    private int helperId;

    @SerializedName("paymentDate")
    private String paymentDate;

    @SerializedName("paymentId")
    private int paymentId;
}

