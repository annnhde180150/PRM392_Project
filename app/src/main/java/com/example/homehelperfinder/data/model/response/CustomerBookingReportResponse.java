package com.example.homehelperfinder.data.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Response model for customer booking report API
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerBookingReportResponse {
    
    @SerializedName("totalBookings")
    private int totalBookings;
    
    @SerializedName("pendingBookings")
    private int pendingBookings;
    
    @SerializedName("confirmedBookings")
    private int confirmedBookings;
    
    @SerializedName("inProgressBookings")
    private int inProgressBookings;
    
    @SerializedName("completedBookings")
    private int completedBookings;
    
    @SerializedName("cancelledBookings")
    private int cancelledBookings;
    
    @SerializedName("averageBookingValue")
    private double averageBookingValue;
    
    @SerializedName("totalBookingValue")
    private double totalBookingValue;
    
    @SerializedName("completionRate")
    private double completionRate;
    
    @SerializedName("cancellationRate")
    private double cancellationRate;
    
    @SerializedName("bookingTrend")
    private List<BookingTrendItem> bookingTrend;
    
    @SerializedName("popularServices")
    private List<Object> popularServices; // Currently empty in API
    
    @SerializedName("peakHours")
    private List<Object> peakHours; // Currently empty in API
    
    @SerializedName("statusBreakdown")
    private List<Object> statusBreakdown; // Currently empty in API
    
    @SerializedName("analyticsPeriodStart")
    private String analyticsPeriodStart;
    
    @SerializedName("analyticsPeriodEnd")
    private String analyticsPeriodEnd;

    /**
     * Get formatted completion rate as percentage
     */
    public String getFormattedCompletionRate() {
        return String.format("%.1f%%", completionRate * 100);
    }

    /**
     * Get formatted cancellation rate as percentage
     */
    public String getFormattedCancellationRate() {
        return String.format("%.1f%%", cancellationRate * 100);
    }

    /**
     * Get formatted average booking value
     */
    public String getFormattedAverageBookingValue() {
        return String.format("%.0f VND", averageBookingValue);
    }

    /**
     * Get formatted total booking value
     */
    public String getFormattedTotalBookingValue() {
        return String.format("%.0f VND", totalBookingValue);
    }

    /**
     * Check if there are any bookings
     */
    public boolean hasBookings() {
        return totalBookings > 0;
    }

    /**
     * Get active bookings count (pending + confirmed + in progress)
     */
    public int getActiveBookings() {
        return pendingBookings + confirmedBookings + inProgressBookings;
    }

    /**
     * Check if booking trend data is available
     */
    public boolean hasBookingTrend() {
        return bookingTrend != null && !bookingTrend.isEmpty();
    }
}
