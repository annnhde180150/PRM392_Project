package com.example.homehelperfinder.data.model.response;

import com.example.homehelperfinder.data.model.Admin;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Response model for admin login
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminLoginResponse {
    @SerializedName("message")
    private String message;
    
    @SerializedName("token")
    private String token;
    
    @SerializedName("admin")
    private Admin admin;
}
