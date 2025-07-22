package com.example.homehelperfinder.data.model.response;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Model for booking trend data in helper reports
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingTrend {
    
    @SerializedName("year")
    private int year;
    
    @SerializedName("month")
    private int month;
    
    @SerializedName("monthName")
    private String monthName;
    
    @SerializedName("totalBookings")
    private int totalBookings;
    
    @SerializedName("completedBookings")
    private int completedBookings;
    
    @SerializedName("cancelledBookings")
    private int cancelledBookings;
    
    @SerializedName("pendingBookings")
    private int pendingBookings;
    
    // Utility methods
    public String getFormattedTotalBookings() {
        return String.valueOf(totalBookings);
    }
    
    public String getFormattedCompletedBookings() {
        return String.valueOf(completedBookings);
    }
    
    public String getFormattedCancelledBookings() {
        return String.valueOf(cancelledBookings);
    }
    
    public String getFormattedPendingBookings() {
        return String.valueOf(pendingBookings);
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
    
    public double getCompletionRate() {
        if (totalBookings == 0) return 0;
        return (double) completedBookings / totalBookings * 100;
    }
    
    public String getFormattedCompletionRate() {
        return String.format("%.1f%%", getCompletionRate());
    }
    
    public double getCancellationRate() {
        if (totalBookings == 0) return 0;
        return (double) cancelledBookings / totalBookings * 100;
    }
    
    public String getFormattedCancellationRate() {
        return String.format("%.1f%%", getCancellationRate());
    }
    
    public boolean hasBookings() {
        return totalBookings > 0;
    }
}
