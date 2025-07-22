package com.example.homehelperfinder.data.model.request

data class BookingCreateRequest(
    val requsetId : Int,
    val userId : Int,
    val serviceId : Int,
    val helperId : Int,
    val scheduledStartTime : String,
    val scheduledEndTime : String,
    val status : String,
    val estimatedPrice : Double,
    val bookingCreationTime : String
)
