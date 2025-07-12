package com.example.homehelperfinder.data.model.request;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePaymentRequest {
    @SerializedName("paymentId")
    private int paymentId;
    @SerializedName("action")
    private String action;
    @SerializedName("paymentDate")
    private String paymentDate;
}
