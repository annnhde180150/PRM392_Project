package com.example.homehelperfinder.data.model.response;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetAllServiceRequestResponse {

    @SerializedName("bookingId")
    private int bookingId;

    @SerializedName("requestId")
    private int requestId;

    @SerializedName("userId")
    private int userId;

    @SerializedName("serviceId")
    private int serviceId;

    @SerializedName("addressId")
    private int addressId;

    @SerializedName("status")
    private String status;

    @SerializedName("scheduledStartTime")
    private String scheduledStartTime;

    @SerializedName("scheduledEndTime")
    private String scheduledEndTime;

    @SerializedName("latitude")
    private Double latitude;

    @SerializedName("longitude")
    private Double longitude;

    @SerializedName("ward")
    private String ward;

    @SerializedName("district")
    private String district;

    @SerializedName("city")
    private String city;

    @SerializedName("fullAddress")
    private String fullAddress;

    @SerializedName("fullName")
    private String fullName;

    @SerializedName("estimatedPrice")
    private Double estimatedPrice;

    @SerializedName("serviceName")
    private String serviceName;
}
