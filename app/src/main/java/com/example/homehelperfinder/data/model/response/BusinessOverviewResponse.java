package com.example.homehelperfinder.data.model.response;

import com.google.gson.annotations.SerializedName;

/**
 * Response model for business overview API
 */
public class BusinessOverviewResponse {
    
    @SerializedName("totalUsers")
    private int totalUsers;
    
    @SerializedName("totalHelpers")
    private int totalHelpers;
    
    @SerializedName("totalBookings")
    private int totalBookings;
    
    @SerializedName("activeBookings")
    private int activeBookings;
    
    @SerializedName("completedBookings")
    private int completedBookings;
    
    @SerializedName("cancelledBookings")
    private int cancelledBookings;
    
    @SerializedName("totalRevenue")
    private double totalRevenue;
    
    @SerializedName("averageRating")
    private double averageRating;
    
    @SerializedName("totalServices")
    private int totalServices;
    
    @SerializedName("totalReviews")
    private int totalReviews;
    
    @SerializedName("lastUpdated")
    private String lastUpdated;
    
    @SerializedName("growthMetrics")
    private GrowthMetrics growthMetrics;
    
    // Getters
    public int getTotalUsers() { return totalUsers; }
    public int getTotalHelpers() { return totalHelpers; }
    public int getTotalBookings() { return totalBookings; }
    public int getActiveBookings() { return activeBookings; }
    public int getCompletedBookings() { return completedBookings; }
    public int getCancelledBookings() { return cancelledBookings; }
    public double getTotalRevenue() { return totalRevenue; }
    public double getAverageRating() { return averageRating; }
    public int getTotalServices() { return totalServices; }
    public int getTotalReviews() { return totalReviews; }
    public String getLastUpdated() { return lastUpdated; }
    public GrowthMetrics getGrowthMetrics() { return growthMetrics; }
    
    // Setters
    public void setTotalUsers(int totalUsers) { this.totalUsers = totalUsers; }
    public void setTotalHelpers(int totalHelpers) { this.totalHelpers = totalHelpers; }
    public void setTotalBookings(int totalBookings) { this.totalBookings = totalBookings; }
    public void setActiveBookings(int activeBookings) { this.activeBookings = activeBookings; }
    public void setCompletedBookings(int completedBookings) { this.completedBookings = completedBookings; }
    public void setCancelledBookings(int cancelledBookings) { this.cancelledBookings = cancelledBookings; }
    public void setTotalRevenue(double totalRevenue) { this.totalRevenue = totalRevenue; }
    public void setAverageRating(double averageRating) { this.averageRating = averageRating; }
    public void setTotalServices(int totalServices) { this.totalServices = totalServices; }
    public void setTotalReviews(int totalReviews) { this.totalReviews = totalReviews; }
    public void setLastUpdated(String lastUpdated) { this.lastUpdated = lastUpdated; }
    public void setGrowthMetrics(GrowthMetrics growthMetrics) { this.growthMetrics = growthMetrics; }
    
    /**
     * Nested class for growth metrics
     */
    public static class GrowthMetrics {
        @SerializedName("userGrowthRate")
        private double userGrowthRate;
        
        @SerializedName("helperGrowthRate")
        private double helperGrowthRate;
        
        @SerializedName("bookingGrowthRate")
        private double bookingGrowthRate;
        
        @SerializedName("revenueGrowthRate")
        private double revenueGrowthRate;
        
        @SerializedName("growthPeriod")
        private String growthPeriod;
        
        // Getters
        public double getUserGrowthRate() { return userGrowthRate; }
        public double getHelperGrowthRate() { return helperGrowthRate; }
        public double getBookingGrowthRate() { return bookingGrowthRate; }
        public double getRevenueGrowthRate() { return revenueGrowthRate; }
        public String getGrowthPeriod() { return growthPeriod; }
        
        // Setters
        public void setUserGrowthRate(double userGrowthRate) { this.userGrowthRate = userGrowthRate; }
        public void setHelperGrowthRate(double helperGrowthRate) { this.helperGrowthRate = helperGrowthRate; }
        public void setBookingGrowthRate(double bookingGrowthRate) { this.bookingGrowthRate = bookingGrowthRate; }
        public void setRevenueGrowthRate(double revenueGrowthRate) { this.revenueGrowthRate = revenueGrowthRate; }
        public void setGrowthPeriod(String growthPeriod) { this.growthPeriod = growthPeriod; }
    }
}
