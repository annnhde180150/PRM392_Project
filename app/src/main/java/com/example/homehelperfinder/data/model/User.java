package com.example.homehelperfinder.data.model;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * User model for login response
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @SerializedName("id")
    private int id;
    
    @SerializedName("phoneNumber")
    private String phoneNumber;
    
    @SerializedName("email")
    private String email;
    
    @SerializedName("fullName")
    private String fullName;
    
    @SerializedName("profilePictureUrl")
    private String profilePictureUrl;
    
    @SerializedName("registrationDate")
    private String registrationDate;
    
    @SerializedName("lastLoginDate")
    private String lastLoginDate;
    
    @SerializedName("externalAuthProvider")
    private String externalAuthProvider;
    
    @SerializedName("externalAuthId")
    private String externalAuthId;
    
    @SerializedName("isActive")
    private boolean isActive;
    
    @SerializedName("defaultAddressId")
    private Integer defaultAddressId;
    
    @SerializedName("role")
    private String role;
}
