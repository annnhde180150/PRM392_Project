package com.example.homehelperfinder.data.model;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
    private Boolean isActive;

    @SerializedName("registrationDate")
    private String registrationDate;

    @SerializedName("lastLoginDate")
    private String lastLoginDate;

    public ProfileModel() {
    }

    public ProfileModel(int profileId, String profileType, String fullName, String email,
                        String phoneNumber, Boolean isActive) {
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

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
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

    public Date getRegistrationDateAsDate() {
        return parseApiDate(registrationDate);
    }

    public Date getLastLoginDateAsDate() {
        return parseApiDate(lastLoginDate);
    }

    private Date parseApiDate(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }

        try {
            SimpleDateFormat inputFormat;
            if (dateString.contains(".")) {
                // Format with milliseconds: 2025-06-20T16:33:01.85
                inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS", Locale.getDefault());
            } else if (dateString.contains("T")) {
                // Format without milliseconds: 2025-06-20T16:33:01
                inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            } else {
                // Simple date format: 2025-06-20
                inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            }
            return inputFormat.parse(dateString);
        } catch (ParseException e) {
            return null;
        }
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
                ", registrationDate=" + registrationDate +
                ", lastLoginDate=" + lastLoginDate +
                '}';
    }
} 