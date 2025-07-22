package com.example.homehelperfinder.data.model.response

data class BookingDetailResponse(
    val bookingId: Int,
    val userId: Int,
    val serviceId: Int,
    val requestId: Int?,
    val helperId: Int,
    val scheduledStartTime: String,
    val scheduledEndTime: String,
    val actualStartTime: String?,
    val actualEndTime: String?,
    val status: String,
    val cancellationReason: String?,
    val cancelledBy: String?,
    val cancellationTime: String?,
    val freeCancellationDeadline: String?,
    val estimatedPrice: Double?,
    val finalPrice: Double?,
    val bookingCreationTime: String?
)
