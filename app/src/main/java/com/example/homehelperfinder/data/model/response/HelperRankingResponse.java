package com.example.homehelperfinder.data.model.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Response model for helper ranking API
 * This represents a single helper ranking item
 */
public class HelperRankingResponse {
    
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
    private int averageResponseTime;
    
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
    
    // Getters
    public int getHelperId() { return helperId; }
    public String getHelperName() { return helperName; }
    public String getEmail() { return email; }
    public int getTotalBookings() { return totalBookings; }
    public int getCompletedBookings() { return completedBookings; }
    public int getCancelledBookings() { return cancelledBookings; }
    public double getCompletionRate() { return completionRate; }
    public double getAverageRating() { return averageRating; }
    public int getTotalReviews() { return totalReviews; }
    public double getTotalEarnings() { return totalEarnings; }
    public double getAverageBookingValue() { return averageBookingValue; }
    public int getAverageResponseTime() { return averageResponseTime; }
    public double getTotalHoursWorked() { return totalHoursWorked; }
    public List<EarningsTrend> getEarningsTrend() { return earningsTrend; }
    public List<ServiceBreakdown> getServiceBreakdown() { return serviceBreakdown; }
    public List<BookingTrend> getBookingTrend() { return bookingTrend; }
    public String getAnalyticsPeriodStart() { return analyticsPeriodStart; }
    public String getAnalyticsPeriodEnd() { return analyticsPeriodEnd; }
    
    // Setters
    public void setHelperId(int helperId) { this.helperId = helperId; }
    public void setHelperName(String helperName) { this.helperName = helperName; }
    public void setEmail(String email) { this.email = email; }
    public void setTotalBookings(int totalBookings) { this.totalBookings = totalBookings; }
    public void setCompletedBookings(int completedBookings) { this.completedBookings = completedBookings; }
    public void setCancelledBookings(int cancelledBookings) { this.cancelledBookings = cancelledBookings; }
    public void setCompletionRate(double completionRate) { this.completionRate = completionRate; }
    public void setAverageRating(double averageRating) { this.averageRating = averageRating; }
    public void setTotalReviews(int totalReviews) { this.totalReviews = totalReviews; }
    public void setTotalEarnings(double totalEarnings) { this.totalEarnings = totalEarnings; }
    public void setAverageBookingValue(double averageBookingValue) { this.averageBookingValue = averageBookingValue; }
    public void setAverageResponseTime(int averageResponseTime) { this.averageResponseTime = averageResponseTime; }
    public void setTotalHoursWorked(double totalHoursWorked) { this.totalHoursWorked = totalHoursWorked; }
    public void setEarningsTrend(List<EarningsTrend> earningsTrend) { this.earningsTrend = earningsTrend; }
    public void setServiceBreakdown(List<ServiceBreakdown> serviceBreakdown) { this.serviceBreakdown = serviceBreakdown; }
    public void setBookingTrend(List<BookingTrend> bookingTrend) { this.bookingTrend = bookingTrend; }
    public void setAnalyticsPeriodStart(String analyticsPeriodStart) { this.analyticsPeriodStart = analyticsPeriodStart; }
    public void setAnalyticsPeriodEnd(String analyticsPeriodEnd) { this.analyticsPeriodEnd = analyticsPeriodEnd; }
    
    // Utility methods
    public String getFormattedEarnings() {
        return String.format("$%.2f", totalEarnings);
    }
    
    public String getFormattedCompletionRate() {
        return String.format("%.1f%%", completionRate);
    }
    
    public String getFormattedRating() {
        if (averageRating == 0) {
            return "No ratings";
        }
        return String.format("%.1f ⭐", averageRating);
    }
    
    /**
     * Nested class for earnings trend data
     */
    public static class EarningsTrend {
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
        
        // Getters and Setters
        public int getYear() { return year; }
        public void setYear(int year) { this.year = year; }
        public int getMonth() { return month; }
        public void setMonth(int month) { this.month = month; }
        public String getMonthName() { return monthName; }
        public void setMonthName(String monthName) { this.monthName = monthName; }
        public double getEarnings() { return earnings; }
        public void setEarnings(double earnings) { this.earnings = earnings; }
        public int getBookingsCount() { return bookingsCount; }
        public void setBookingsCount(int bookingsCount) { this.bookingsCount = bookingsCount; }
    }
    
    /**
     * Nested class for service breakdown data
     */
    public static class ServiceBreakdown {
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
        
        // Getters and Setters
        public int getServiceId() { return serviceId; }
        public void setServiceId(int serviceId) { this.serviceId = serviceId; }
        public String getServiceName() { return serviceName; }
        public void setServiceName(String serviceName) { this.serviceName = serviceName; }
        public int getBookingsCount() { return bookingsCount; }
        public void setBookingsCount(int bookingsCount) { this.bookingsCount = bookingsCount; }
        public double getTotalEarnings() { return totalEarnings; }
        public void setTotalEarnings(double totalEarnings) { this.totalEarnings = totalEarnings; }
        public double getAverageRating() { return averageRating; }
        public void setAverageRating(double averageRating) { this.averageRating = averageRating; }
        public double getCompletionRate() { return completionRate; }
        public void setCompletionRate(double completionRate) { this.completionRate = completionRate; }
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
}
