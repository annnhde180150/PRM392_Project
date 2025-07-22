package com.example.homehelperfinder.data.model.response;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Model for service breakdown data in helper earnings report
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceBreakdown {
    
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
    
    // Utility methods
    public String getFormattedBookingsCount() {
        return String.valueOf(bookingsCount);
    }
    
    public String getFormattedTotalEarnings() {
        return String.format("%,.0f VNĐ", totalEarnings);
    }
    
    public String getFormattedAverageRating() {
        return String.format("%.1f", averageRating);
    }
    
    public String getFormattedCompletionRate() {
        return String.format("%.1f%%", completionRate);
    }
    
    public double getAverageEarningsPerBooking() {
        if (bookingsCount == 0) return 0;
        return totalEarnings / bookingsCount;
    }
    
    public String getFormattedAverageEarningsPerBooking() {
        return String.format("%,.0f VNĐ", getAverageEarningsPerBooking());
    }
    
    public boolean hasEarnings() {
        return totalEarnings > 0;
    }
    
    public boolean hasBookings() {
        return bookingsCount > 0;
    }
    
    public boolean hasRating() {
        return averageRating > 0;
    }
    
    public String getRatingStars() {
        int fullStars = (int) averageRating;
        StringBuilder stars = new StringBuilder();
        
        for (int i = 0; i < 5; i++) {
            if (i < fullStars) {
                stars.append("★");
            } else {
                stars.append("☆");
            }
        }
        
        return stars.toString();
    }
    
    public String getServiceDisplayName() {
        return serviceName != null ? serviceName : "Dịch vụ không xác định";
    }
}
