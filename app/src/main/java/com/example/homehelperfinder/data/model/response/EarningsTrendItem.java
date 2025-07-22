package com.example.homehelperfinder.data.model.response;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Model for earnings trend data in favorite helpers report
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EarningsTrendItem {
    
    @SerializedName("year")
    private int year;
    
    @SerializedName("month")
    private int month;
    
    @SerializedName("monthName")
    private String monthName;
    
    @SerializedName("earnings")
    private double earnings;
    
    @SerializedName("bookingsCount")
    private int bookingsCount;

    /**
     * Get formatted earnings
     */
    public String getFormattedEarnings() {
        return String.format("%.0f VND", earnings);
    }

    /**
     * Get period display text
     */
    public String getPeriodDisplay() {
        return monthName + " " + year;
    }

    /**
     * Check if there are any earnings
     */
    public boolean hasEarnings() {
        return earnings > 0;
    }

    /**
     * Check if there are any bookings
     */
    public boolean hasBookings() {
        return bookingsCount > 0;
    }

    /**
     * Get average earnings per booking
     */
    public double getAverageEarningsPerBooking() {
        if (bookingsCount == 0) return 0;
        return earnings / bookingsCount;
    }

    /**
     * Get formatted average earnings per booking
     */
    public String getFormattedAverageEarningsPerBooking() {
        return String.format("%.0f VND", getAverageEarningsPerBooking());
    }
}
