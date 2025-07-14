package com.example.homehelperfinder.data.model.request;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class HelperWorkAreaRequest {

    @SerializedName("city")
    public String City;

    @SerializedName("district")
    public String District;

    @SerializedName("ward")
    public String Ward;

    @SerializedName("latitude")
    public double Latitude;

    @SerializedName("longitude")
    public double Longitude;

    @SerializedName("radiusKm")
    public double RadiusKm;
}
