package com.example.homehelperfinder.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Model for individual request item in admin requests list
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestItem {
    @SerializedName("requestId")
    private int requestId;
    
    @SerializedName("user")
    private RequestUser user;
    
    @SerializedName("helper")
    private RequestHelper helper;
    
    @SerializedName("services")
    private List<String> services;
    
    @SerializedName("scheduledTime")
    private String scheduledTime;
    
    @SerializedName("location")
    private String location;
    
    @SerializedName("status")
    private String status;
    
    @SerializedName("requestCreationTime")
    private String requestCreationTime;
    
    @SerializedName("specialNotes")
    private String specialNotes;
    
    @SerializedName("requestedDurationHours")
    private double requestedDurationHours;
    
    /**
     * Get formatted services as comma-separated string
     */
    public String getServicesAsString() {
        if (services == null || services.isEmpty()) {
            return "No services";
        }
        return String.join(", ", services);
    }
    
    /**
     * Get status with proper formatting
     */
    public String getFormattedStatus() {
        if (status == null) return "Unknown";
        return status.substring(0, 1).toUpperCase() + status.substring(1).toLowerCase();
    }
    
    /**
     * Check if request has helper assigned
     */
    public boolean hasHelper() {
        return helper != null && helper.getId() > 0;
    }
}
