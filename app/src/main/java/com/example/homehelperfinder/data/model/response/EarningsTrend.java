package com.example.homehelperfinder.data.model.response;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Model for earnings trend data in helper reports
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EarningsTrend {
    
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
    
    // Utility methods
    public String getFormattedEarnings() {
        return String.format("%,.0f VNĐ", earnings);
    }
    
    public String getFormattedBookingsCount() {
        return String.valueOf(bookingsCount);
    }
    
    public String getFormattedPeriod() {
        return monthName + " " + year;
    }
    
    public String getShortMonthName() {
        if (monthName == null) return "";
        
        // Convert Vietnamese month names to short format
        switch (monthName.toLowerCase()) {
            case "tháng 1": return "T1";
            case "tháng 2": return "T2";
            case "tháng 3": return "T3";
            case "tháng 4": return "T4";
            case "tháng 5": return "T5";
            case "tháng 6": return "T6";
            case "tháng 7": return "T7";
            case "tháng 8": return "T8";
            case "tháng 9": return "T9";
            case "tháng 10": return "T10";
            case "tháng 11": return "T11";
            case "tháng 12": return "T12";
            default: return monthName;
        }
    }
    
    public double getAverageEarningsPerBooking() {
        if (bookingsCount == 0) return 0;
        return earnings / bookingsCount;
    }
    
    public String getFormattedAverageEarningsPerBooking() {
        return String.format("%,.0f VNĐ", getAverageEarningsPerBooking());
    }
    
    public boolean hasEarnings() {
        return earnings > 0;
    }
    
    public boolean hasBookings() {
        return bookingsCount > 0;
    }
}
