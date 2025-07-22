package com.example.homehelperfinder.data.model.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Response model for helper schedule analytics API
 * Endpoint: /api/Report/helper/my-schedule-analytics
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HelperScheduleAnalyticsResponse {
    
    @SerializedName("totalHoursWorked")
    private double totalHoursWorked;
    
    @SerializedName("averageBookingValue")
    private double averageBookingValue;
    
    @SerializedName("bookingTrend")
    private List<BookingTrend> bookingTrend;
    
    @SerializedName("earningsTrend")
    private List<EarningsTrend> earningsTrend;
    
    @SerializedName("period")
    private AnalyticsPeriod period;
    
    // Utility methods
    public String getFormattedTotalHours() {
        return String.format("%.1f giờ", totalHoursWorked);
    }
    
    public String getFormattedAverageBookingValue() {
        return String.format("%,.0f VNĐ", averageBookingValue);
    }
    
    public boolean hasBookingTrend() {
        return bookingTrend != null && !bookingTrend.isEmpty();
    }
    
    public boolean hasEarningsTrend() {
        return earningsTrend != null && !earningsTrend.isEmpty();
    }
    
    public double getAverageHoursPerDay() {
        if (period == null) return 0;
        
        // Calculate days between start and end
        // For simplicity, assume 30 days for month period
        double days = 30; // This could be calculated more precisely
        
        return totalHoursWorked / days;
    }
    
    public String getFormattedAverageHoursPerDay() {
        return String.format("%.1f giờ/ngày", getAverageHoursPerDay());
    }
    
    public double getTotalEarnings() {
        if (earningsTrend == null) return 0;
        
        return earningsTrend.stream()
                .mapToDouble(EarningsTrend::getEarnings)
                .sum();
    }
    
    public String getFormattedTotalEarnings() {
        return String.format("%,.0f VNĐ", getTotalEarnings());
    }
    
    public int getTotalBookings() {
        if (bookingTrend == null) return 0;
        
        return bookingTrend.stream()
                .mapToInt(BookingTrend::getTotalBookings)
                .sum();
    }
    
    public String getFormattedTotalBookings() {
        return String.valueOf(getTotalBookings());
    }
    
    public double getHourlyRate() {
        if (totalHoursWorked == 0) return 0;
        return getTotalEarnings() / totalHoursWorked;
    }
    
    public String getFormattedHourlyRate() {
        return String.format("%,.0f VNĐ/giờ", getHourlyRate());
    }
}
