package com.example.homehelperfinder.data.model.response

import com.google.gson.annotations.SerializedName

data class RequestDetailResponse(
    @SerializedName("requestId")
    val requestId : Int,

    @SerializedName("userId")
    val userId : Int,

    @SerializedName("serviceId")
    val serviceId : Int,

    @SerializedName("addressId")
    val addressId : Int,

    @SerializedName("requestedStartTime")
    val requestedStartTime : String,

    @SerializedName("requestedDurationHours")
    val requestedDurationHours : Int,

    @SerializedName("specialNotes")
    val specialNotes : String,

    @SerializedName("status")
    val status : String,

    @SerializedName("requestCreationTime")
    val requestCreationTime : String,

    @SerializedName("latitude")
    val latitude : Double,

    @SerializedName("longitude")
    val longitude : Double
)
