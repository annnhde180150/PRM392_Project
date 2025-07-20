package com.example.homehelperfinder.data.model.request;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request model for admin profile update
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminUpdateRequest {
    @SerializedName("username")
    private String username;
    
    @SerializedName("fullName")
    private String fullName;
    
    @SerializedName("email")
    private String email;
    
    @SerializedName("role")
    private String role;
    
    @SerializedName("isActive")
    private boolean isActive;
} 