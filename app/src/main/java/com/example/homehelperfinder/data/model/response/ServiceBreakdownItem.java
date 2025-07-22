package com.example.homehelperfinder.data.model.response;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Model for service breakdown data in favorite helpers report
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceBreakdownItem {
    
    @SerializedName("serviceId")
    private int serviceId;
    
    @SerializedName("serviceName")
    private String serviceName;
    
    @SerializedName("bookingsCount")
    private int bookingsCount;
    
    @SerializedName("totalEarnings")
    private double totalEarnings;
    
    @SerializedName("averageRating")
    private double averageRating;
    
    @SerializedName("completionRate")
    private double completionRate;

    /**
     * Get formatted total earnings
     */
    public String getFormattedTotalEarnings() {
        return String.format("%.0f VND", totalEarnings);
    }

    /**
     * Get formatted average rating
     */
    public String getFormattedAverageRating() {
        if (averageRating == 0) {
            return "Chưa có đánh giá";
        }
        return String.format("%.1f", averageRating);
    }

    /**
     * Get formatted completion rate as percentage
     */
    public String getFormattedCompletionRate() {
        return String.format("%.1f%%", completionRate * 100);
    }

    /**
     * Check if there are any bookings
     */
    public boolean hasBookings() {
        return bookingsCount > 0;
    }

    /**
     * Check if there are any earnings
     */
    public boolean hasEarnings() {
        return totalEarnings > 0;
    }

    /**
     * Check if there is a rating
     */
    public boolean hasRating() {
        return averageRating > 0;
    }

    /**
     * Get average earnings per booking
     */
    public double getAverageEarningsPerBooking() {
        if (bookingsCount == 0) return 0;
        return totalEarnings / bookingsCount;
    }

    /**
     * Get formatted average earnings per booking
     */
    public String getFormattedAverageEarningsPerBooking() {
        return String.format("%.0f VND", getAverageEarningsPerBooking());
    }
}
