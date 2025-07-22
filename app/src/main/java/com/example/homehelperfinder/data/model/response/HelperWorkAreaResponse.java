package com.example.homehelperfinder.data.model.response;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HelperWorkAreaResponse {
    @SerializedName("workAreaId")
    private Integer workAreaId;
    
    @SerializedName("city")
    private String city;
    
    @SerializedName("district")
    private String district;
    
    @SerializedName("ward")
    private String ward;
    
    @SerializedName("latitude")
    private Double latitude;
    
    @SerializedName("longitude")
    private Double longitude;
    
    @SerializedName("radiusKm")
    private Double radiusKm;
} 