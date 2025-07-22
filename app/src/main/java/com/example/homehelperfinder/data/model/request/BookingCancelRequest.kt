package com.example.homehelperfinder.data.model.request

data class BookingCancelRequest(
    val bookingId: Int,
    val cancellationReason: String,
    val cancelledBy: String
)
