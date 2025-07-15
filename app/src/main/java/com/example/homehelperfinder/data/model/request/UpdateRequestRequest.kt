package com.example.homehelperfinder.data.model.request

import com.google.gson.annotations.SerializedName

data class UpdateRequestRequest (
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
    val requestedDurationHours : Double,

    @SerializedName("specialNotes")
    val specialNote : String,

    @SerializedName("status")
    val status : String,

    )