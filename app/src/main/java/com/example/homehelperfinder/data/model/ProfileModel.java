package com.example.homehelperfinder.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Model class representing a Profile from the API
 */
public class ProfileModel {
    @SerializedName("profileId")
    private int profileId;

    @SerializedName("profileType")
    private String profileType;

    @SerializedName("fullName")
    private String fullName;

    @SerializedName("email")
    private String email;

    @SerializedName("phoneNumber")
    private String phoneNumber;

    @SerializedName("isActive")
    private boolean isActive;

    @SerializedName("registrationDate")
    private String registrationDate;

    @SerializedName("lastLoginDate")
    private String lastLoginDate;

    // Legacy fields (for banned profiles - might be null in some responses)
    @SerializedName("isBanned")
    private Boolean isBanned;

    @SerializedName("banReason")
    private String banReason;

    @SerializedName("bannedAt")
    private String bannedAt;

    @SerializedName("bannedBy")
    private Integer bannedBy;

    @SerializedName("banDuration")
    private Integer banDuration;

    @SerializedName("status")
    private String status;

    // Default constructor
    public ProfileModel() {}

    // Constructor with main fields
    public ProfileModel(int profileId, String profileType, String fullName, String email, 
                       String phoneNumber, boolean isActive) {
        this.profileId = profileId;
        this.profileType = profileType;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.isActive = isActive;
    }

    // Getters and Setters
    public int getProfileId() {
        return profileId;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }

    public String getProfileType() {
        return profileType;
    }

    public void setProfileType(String profileType) {
        this.profileType = profileType;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(String lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    // Legacy fields getters/setters
    public Boolean isBanned() {
        return isBanned;
    }

    public void setBanned(Boolean banned) {
        isBanned = banned;
    }

    public String getBanReason() {
        return banReason;
    }

    public void setBanReason(String banReason) {
        this.banReason = banReason;
    }

    public String getBannedAt() {
        return bannedAt;
    }

    public void setBannedAt(String bannedAt) {
        this.bannedAt = bannedAt;
    }

    public Integer getBannedBy() {
        return bannedBy;
    }

    public void setBannedBy(Integer bannedBy) {
        this.bannedBy = bannedBy;
    }

    public Integer getBanDuration() {
        return banDuration;
    }

    public void setBanDuration(Integer banDuration) {
        this.banDuration = banDuration;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Helper method to determine if profile is banned
    public boolean getIsBanned() {
        return isBanned != null ? isBanned : !isActive;
    }

    @Override
    public String toString() {
        return "ProfileModel{" +
                "profileId=" + profileId +
                ", profileType='" + profileType + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", isActive=" + isActive +
                ", registrationDate='" + registrationDate + '\'' +
                ", lastLoginDate='" + lastLoginDate + '\'' +
                ", isBanned=" + isBanned +
                ", banReason='" + banReason + '\'' +
                ", bannedAt='" + bannedAt + '\'' +
                ", bannedBy=" + bannedBy +
                ", banDuration=" + banDuration +
                ", status='" + status + '\'' +
                '}';
    }
} 