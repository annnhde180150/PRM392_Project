package com.example.homehelperfinder.data.model;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Model for helper work area information
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HelperWorkArea {
    @SerializedName("workAreaId")
    private int workAreaId;
    
    @SerializedName("city")
    private String city;
    
    @SerializedName("district")
    private String district;
    
    @SerializedName("ward")
    private String ward;
    
    @SerializedName("latitude")
    private double latitude;
    
    @SerializedName("longitude")
    private double longitude;
    
    @SerializedName("radiusKm")
    private double radiusKm;
    
    /**
     * Get formatted location string
     */
    public String getFormattedLocation() {
        StringBuilder location = new StringBuilder();
        
        if (ward != null && !ward.trim().isEmpty()) {
            location.append(ward).append(", ");
        }
        
        if (district != null && !district.trim().isEmpty()) {
            location.append(district).append(", ");
        }
        
        if (city != null && !city.trim().isEmpty()) {
            location.append(city);
        }
        
        return location.toString();
    }
    
    /**
     * Get formatted radius
     */
    public String getFormattedRadius() {
        if (radiusKm == (int) radiusKm) {
            return String.format("%d km", (int) radiusKm);
        } else {
            return String.format("%.1f km", radiusKm);
        }
    }
    
    /**
     * Get full location with radius
     */
    public String getFullLocationDescription() {
        return getFormattedLocation() + " (Radius: " + getFormattedRadius() + ")";
    }
    
    /**
     * Check if ward is specified
     */
    public boolean hasWard() {
        return ward != null && !ward.trim().isEmpty();
    }
}
