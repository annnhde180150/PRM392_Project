package com.example.homehelperfinder.data.model;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Admin model for login response
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Admin {
    @SerializedName("id")
    private int id;
    
    @SerializedName("username")
    private String username;
    
    @SerializedName("fullName")
    private String fullName;
    
    @SerializedName("email")
    private String email;
    
    @SerializedName("creationDate")
    private String creationDate;
    
    @SerializedName("lastLoginDate")
    private String lastLoginDate;
    
    @SerializedName("isActive")
    private boolean isActive;
    
    @SerializedName("role")
    private String role;
}
