package com.example.homehelperfinder.data.model;

import com.google.gson.annotations.SerializedName;

public class BanUnbanRequest {
    @SerializedName("profileId")
    private int profileId;

    @SerializedName("profileType")
    private String profileType;

    @SerializedName("reason")
    private String reason;

    public BanUnbanRequest() {}

    public BanUnbanRequest(int profileId, String profileType, String reason) {
        this.profileId = profileId;
        this.profileType = profileType;
        this.reason = reason;
    }

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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
    @Override
    public String toString() {
        return "BanUnbanRequest{" +
                "profileId=" + profileId +
                ", profileType='" + profileType + '\'' +
                ", reason='" + reason + '\'' +
                '}';
    }
}
