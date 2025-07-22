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
public class BookingStatusUpdateRequest {
    @SerializedName("bookingId")
    private int bookingId;
    
    @SerializedName("helperId")
    private int helperId;
    
    @SerializedName("status")
    private String status;
} 