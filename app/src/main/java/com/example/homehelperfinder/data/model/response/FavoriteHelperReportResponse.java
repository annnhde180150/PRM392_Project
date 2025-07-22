package com.example.homehelperfinder.data.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Response model for favorite helpers report API
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteHelperReportResponse {
    
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
    private List<EarningsTrendItem> earningsTrend;
    
    @SerializedName("serviceBreakdown")
    private List<ServiceBreakdownItem> serviceBreakdown;
    
    @SerializedName("bookingTrend")
    private List<BookingTrendItem> bookingTrend;
    
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
     * Get formatted average rating
     */
    public String getFormattedAverageRating() {
        if (averageRating == 0) {
            return "Chưa có đánh giá";
        }
        return String.format("%.1f", averageRating);
    }

    /**
     * Get formatted total earnings
     */
    public String getFormattedTotalEarnings() {
        return String.format("%.0f VND", totalEarnings);
    }

    /**
     * Get formatted average booking value
     */
    public String getFormattedAverageBookingValue() {
        return String.format("%.0f VND", averageBookingValue);
    }

    /**
     * Get formatted total hours worked
     */
    public String getFormattedTotalHoursWorked() {
        return String.format("%.1f giờ", totalHoursWorked);
    }

    /**
     * Get formatted average response time
     */
    public String getFormattedAverageResponseTime() {
        if (averageResponseTime < 60) {
            return String.format("%.0f phút", averageResponseTime);
        } else {
            return String.format("%.1f giờ", averageResponseTime / 60);
        }
    }

    /**
     * Check if helper has any bookings
     */
    public boolean hasBookings() {
        return totalBookings > 0;
    }

    /**
     * Check if helper has any earnings
     */
    public boolean hasEarnings() {
        return totalEarnings > 0;
    }

    /**
     * Check if helper has any reviews
     */
    public boolean hasReviews() {
        return totalReviews > 0;
    }

    /**
     * Get active bookings (total - cancelled)
     */
    public int getActiveBookings() {
        return totalBookings - cancelledBookings;
    }

    /**
     * Check if earnings trend data is available
     */
    public boolean hasEarningsTrend() {
        return earningsTrend != null && !earningsTrend.isEmpty();
    }

    /**
     * Check if service breakdown data is available
     */
    public boolean hasServiceBreakdown() {
        return serviceBreakdown != null && !serviceBreakdown.isEmpty();
    }

    /**
     * Check if booking trend data is available
     */
    public boolean hasBookingTrend() {
        return bookingTrend != null && !bookingTrend.isEmpty();
    }
}
