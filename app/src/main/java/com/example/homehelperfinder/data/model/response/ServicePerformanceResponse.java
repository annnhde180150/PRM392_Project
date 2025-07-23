package com.example.homehelperfinder.data.model.response;

import com.google.gson.annotations.SerializedName;

/**
 * Response model for service performance API
 * This represents a single service performance item
 */
public class ServicePerformanceResponse {
    
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
    
    // Getters
    public int getServiceId() {
        return serviceId;
    }
    
    public String getServiceName() {
        return serviceName;
    }
    
    public int getBookingsCount() {
        return bookingsCount;
    }
    
    public double getTotalRevenue() {
        return totalRevenue;
    }
    
    public double getAverageRating() {
        return averageRating;
    }
    
    public double getMarketShare() {
        return marketShare;
    }
    
    // Setters
    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }
    
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
    
    public void setBookingsCount(int bookingsCount) {
        this.bookingsCount = bookingsCount;
    }
    
    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
    
    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }
    
    public void setMarketShare(double marketShare) {
        this.marketShare = marketShare;
    }
    
    /**
     * Get formatted revenue string
     */
    public String getFormattedRevenue() {
        return String.format("$%.2f", totalRevenue);
    }
    
    /**
     * Get formatted market share string
     */
    public String getFormattedMarketShare() {
        return String.format("%.1f%%", marketShare);
    }
    
    /**
     * Get formatted rating string
     */
    public String getFormattedRating() {
        if (averageRating == 0) {
            return "No ratings";
        }
        return String.format("%.1f ⭐", averageRating);
    }
    
    /**
     * Get bookings count string
     */
    public String getFormattedBookingsCount() {
        return bookingsCount + " bookings";
    }
}
