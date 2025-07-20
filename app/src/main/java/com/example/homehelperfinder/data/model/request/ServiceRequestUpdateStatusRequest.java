package com.example.homehelperfinder.data.model.request;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceRequestUpdateStatusRequest {
    @SerializedName("requestId")
    public Integer RequestId;

    @SerializedName("bookingId")
    public Integer BookingId;

    @SerializedName("action")
    public String Action;

}
