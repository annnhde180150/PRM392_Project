package com.example.homehelperfinder.models;

public class HelperWorkArea {
    private String city;
    private String district;
    private String ward;
    private Double latitude;
    private Double longitude;
    private Double radiusKm;

    public HelperWorkArea(String city, String district, String ward, Double latitude, Double longitude, Double radiusKm) {
        this.city = city;
        this.district = district;
        this.ward = ward;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radiusKm = radiusKm;
    }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }
    public String getWard() { return ward; }
    public void setWard(String ward) { this.ward = ward; }
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public Double getRadiusKm() { return radiusKm; }
    public void setRadiusKm(Double radiusKm) { this.radiusKm = radiusKm; }
} 