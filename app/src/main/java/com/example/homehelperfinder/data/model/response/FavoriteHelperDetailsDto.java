package com.example.homehelperfinder.data.model.response;

public class FavoriteHelperDetailsDto {
    private int helperId;
    private String fullName;
    private String email;
    private String profilePictureUrl;

    public int getHelperId() {
        return helperId;
    }
    public void setHelperId(int helperId) {
        this.helperId = helperId;
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
    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }
    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }
} 