package com.example.homehelperfinder.data.model.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Response model for helper earnings report API
 * Endpoint: /api/Report/helper/my-earnings
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HelperEarningsReportResponse {
    
    @SerializedName("helperId")
    private int helperId;
    
    @SerializedName("helperName")
    private String helperName;
    
    @SerializedName("email")
    private String email;
    
    @SerializedName("totalBookings")
    private int totalBookings;
    
    @SerializedName("completedBookings")
    private int completedBookings;
    
    @SerializedName("cancelledBookings")
    private int cancelledBookings;
    
    @SerializedName("completionRate")
    private double completionRate;
    
    @SerializedName("averageRating")
    private double averageRating;
    
    @SerializedName("totalReviews")
    private int totalReviews;
    
    @SerializedName("totalEarnings")
    private double totalEarnings;
    
    @SerializedName("averageBookingValue")
    private double averageBookingValue;
    
    @SerializedName("averageResponseTime")
    private double averageResponseTime;
    
    @SerializedName("totalHoursWorked")
    private double totalHoursWorked;
    
    @SerializedName("earningsTrend")
    private List<EarningsTrend> earningsTrend;
    
    @SerializedName("serviceBreakdown")
    private List<ServiceBreakdown> serviceBreakdown;
    
    @SerializedName("bookingTrend")
    private List<BookingTrend> bookingTrend;
    
    @SerializedName("analyticsPeriodStart")
    private String analyticsPeriodStart;
    
    @SerializedName("analyticsPeriodEnd")
    private String analyticsPeriodEnd;
    
    // Utility methods
    public String getFormattedCompletionRate() {
        return String.format("%.1f%%", completionRate);
    }
    
    public String getFormattedAverageRating() {
        return String.format("%.1f", averageRating);
    }
    
    public String getFormattedTotalEarnings() {
        return String.format("%,.0f VNĐ", totalEarnings);
    }
    
    public String getFormattedAverageBookingValue() {
        return String.format("%,.0f VNĐ", averageBookingValue);
    }
    
    public String getFormattedTotalHours() {
        return String.format("%.1f giờ", totalHoursWorked);
    }
    
    public String getFormattedAverageResponseTime() {
        if (averageResponseTime < 60) {
            return String.format("%.0f phút", averageResponseTime);
        } else {
            double hours = averageResponseTime / 60;
            return String.format("%.1f giờ", hours);
        }
    }
    
    public boolean hasEarningsTrend() {
        return earningsTrend != null && !earningsTrend.isEmpty();
    }
    
    public boolean hasServiceBreakdown() {
        return serviceBreakdown != null && !serviceBreakdown.isEmpty();
    }
    
    public boolean hasBookingTrend() {
        return bookingTrend != null && !bookingTrend.isEmpty();
    }
    
    public int getPendingBookings() {
        return totalBookings - completedBookings - cancelledBookings;
    }
    
    public String getFormattedPendingBookings() {
        return String.valueOf(getPendingBookings());
    }
}
