package com.example.homehelperfinder.data.model.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Response model for booking analytics API
 */
public class BookingAnalyticsResponse {
    
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
    private List<BookingTrend> bookingTrend;
    
    @SerializedName("popularServices")
    private List<PopularService> popularServices;
    
    @SerializedName("peakHours")
    private List<PeakHour> peakHours;
    
    @SerializedName("statusBreakdown")
    private List<StatusBreakdown> statusBreakdown;
    
    @SerializedName("analyticsPeriodStart")
    private String analyticsPeriodStart;
    
    @SerializedName("analyticsPeriodEnd")
    private String analyticsPeriodEnd;
    
    // Getters
    public int getTotalBookings() { return totalBookings; }
    public int getPendingBookings() { return pendingBookings; }
    public int getConfirmedBookings() { return confirmedBookings; }
    public int getInProgressBookings() { return inProgressBookings; }
    public int getCompletedBookings() { return completedBookings; }
    public int getCancelledBookings() { return cancelledBookings; }
    public double getAverageBookingValue() { return averageBookingValue; }
    public double getTotalBookingValue() { return totalBookingValue; }
    public double getCompletionRate() { return completionRate; }
    public double getCancellationRate() { return cancellationRate; }
    public List<BookingTrend> getBookingTrend() { return bookingTrend; }
    public List<PopularService> getPopularServices() { return popularServices; }
    public List<PeakHour> getPeakHours() { return peakHours; }
    public List<StatusBreakdown> getStatusBreakdown() { return statusBreakdown; }
    public String getAnalyticsPeriodStart() { return analyticsPeriodStart; }
    public String getAnalyticsPeriodEnd() { return analyticsPeriodEnd; }
    
    // Setters
    public void setTotalBookings(int totalBookings) { this.totalBookings = totalBookings; }
    public void setPendingBookings(int pendingBookings) { this.pendingBookings = pendingBookings; }
    public void setConfirmedBookings(int confirmedBookings) { this.confirmedBookings = confirmedBookings; }
    public void setInProgressBookings(int inProgressBookings) { this.inProgressBookings = inProgressBookings; }
    public void setCompletedBookings(int completedBookings) { this.completedBookings = completedBookings; }
    public void setCancelledBookings(int cancelledBookings) { this.cancelledBookings = cancelledBookings; }
    public void setAverageBookingValue(double averageBookingValue) { this.averageBookingValue = averageBookingValue; }
    public void setTotalBookingValue(double totalBookingValue) { this.totalBookingValue = totalBookingValue; }
    public void setCompletionRate(double completionRate) { this.completionRate = completionRate; }
    public void setCancellationRate(double cancellationRate) { this.cancellationRate = cancellationRate; }
    public void setBookingTrend(List<BookingTrend> bookingTrend) { this.bookingTrend = bookingTrend; }
    public void setPopularServices(List<PopularService> popularServices) { this.popularServices = popularServices; }
    public void setPeakHours(List<PeakHour> peakHours) { this.peakHours = peakHours; }
    public void setStatusBreakdown(List<StatusBreakdown> statusBreakdown) { this.statusBreakdown = statusBreakdown; }
    public void setAnalyticsPeriodStart(String analyticsPeriodStart) { this.analyticsPeriodStart = analyticsPeriodStart; }
    public void setAnalyticsPeriodEnd(String analyticsPeriodEnd) { this.analyticsPeriodEnd = analyticsPeriodEnd; }
    
    // Utility methods
    public String getFormattedAverageBookingValue() {
        return String.format("$%.2f", averageBookingValue);
    }
    
    public String getFormattedTotalBookingValue() {
        return String.format("$%.2f", totalBookingValue);
    }
    
    public String getFormattedCompletionRate() {
        return String.format("%.1f%%", completionRate);
    }
    
    public String getFormattedCancellationRate() {
        return String.format("%.1f%%", cancellationRate);
    }
    
    /**
     * Nested class for booking trend data
     */
    public static class BookingTrend {
        @SerializedName("date")
        private String date;
        
        @SerializedName("bookingsCount")
        private int bookingsCount;
        
        // Getters and Setters
        public String getDate() { return date; }
        public void setDate(String date) { this.date = date; }
        public int getBookingsCount() { return bookingsCount; }
        public void setBookingsCount(int bookingsCount) { this.bookingsCount = bookingsCount; }
    }
    
    /**
     * Nested class for popular service data
     */
    public static class PopularService {
        @SerializedName("serviceId")
        private int serviceId;
        
        @SerializedName("serviceName")
        private String serviceName;
        
        @SerializedName("bookingsCount")
        private int bookingsCount;
        
        @SerializedName("totalRevenue")
        private double totalRevenue;
        
        @SerializedName("averageRating")
        private double averageRating;
        
        @SerializedName("marketShare")
        private double marketShare;
        
        // Getters and Setters
        public int getServiceId() { return serviceId; }
        public void setServiceId(int serviceId) { this.serviceId = serviceId; }
        public String getServiceName() { return serviceName; }
        public void setServiceName(String serviceName) { this.serviceName = serviceName; }
        public int getBookingsCount() { return bookingsCount; }
        public void setBookingsCount(int bookingsCount) { this.bookingsCount = bookingsCount; }
        public double getTotalRevenue() { return totalRevenue; }
        public void setTotalRevenue(double totalRevenue) { this.totalRevenue = totalRevenue; }
        public double getAverageRating() { return averageRating; }
        public void setAverageRating(double averageRating) { this.averageRating = averageRating; }
        public double getMarketShare() { return marketShare; }
        public void setMarketShare(double marketShare) { this.marketShare = marketShare; }
    }
    
    /**
     * Nested class for peak hour data
     */
    public static class PeakHour {
        @SerializedName("hour")
        private int hour;
        
        @SerializedName("timeRange")
        private String timeRange;
        
        @SerializedName("bookingsCount")
        private int bookingsCount;
        
        @SerializedName("percentage")
        private double percentage;
        
        // Getters and Setters
        public int getHour() { return hour; }
        public void setHour(int hour) { this.hour = hour; }
        public String getTimeRange() { return timeRange; }
        public void setTimeRange(String timeRange) { this.timeRange = timeRange; }
        public int getBookingsCount() { return bookingsCount; }
        public void setBookingsCount(int bookingsCount) { this.bookingsCount = bookingsCount; }
        public double getPercentage() { return percentage; }
        public void setPercentage(double percentage) { this.percentage = percentage; }
        
        public String getFormattedPercentage() {
            return String.format("%.1f%%", percentage);
        }
    }
    
    /**
     * Nested class for status breakdown data
     */
    public static class StatusBreakdown {
        @SerializedName("status")
        private String status;
        
        @SerializedName("count")
        private int count;
        
        @SerializedName("percentage")
        private double percentage;
        
        // Getters and Setters
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public int getCount() { return count; }
        public void setCount(int count) { this.count = count; }
        public double getPercentage() { return percentage; }
        public void setPercentage(double percentage) { this.percentage = percentage; }
        
        public String getFormattedPercentage() {
            return String.format("%.1f%%", percentage);
        }
    }
}
