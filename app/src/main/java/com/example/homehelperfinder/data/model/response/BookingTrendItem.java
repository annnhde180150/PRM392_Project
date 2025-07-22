package com.example.homehelperfinder.data.model.response;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Model for booking trend data in customer reports
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingTrendItem {
    
    @SerializedName("date")
    private String date;
    
    @SerializedName("bookingsCount")
    private int bookingsCount;
    
    @SerializedName("earningsAmount")
    private double earningsAmount;

    /**
     * Get formatted date for display
     */
    public String getFormattedDate() {
        // You can implement date formatting logic here
        return date;
    }

    /**
     * Get formatted earnings amount
     */
    public String getFormattedEarnings() {
        return String.format("%.0f VND", earningsAmount);
    }

    /**
     * Check if this item has any bookings
     */
    public boolean hasBookings() {
        return bookingsCount > 0;
    }

    /**
     * Check if this item has any earnings
     */
    public boolean hasEarnings() {
        return earningsAmount > 0;
    }
}
