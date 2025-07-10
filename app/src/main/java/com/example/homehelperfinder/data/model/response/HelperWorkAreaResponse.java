package com.example.homehelperfinder.data.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HelperWorkAreaResponse {
    private String city;
    private String district;
    private String ward;
    private Double latitude;
    private Double longitude;
    private Double radiusKm;
} 