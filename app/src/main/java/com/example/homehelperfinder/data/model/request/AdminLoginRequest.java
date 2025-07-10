package com.example.homehelperfinder.data.model.request;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request model for admin login
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminLoginRequest {
    @SerializedName("username")
    private String username;
    
    @SerializedName("password")
    private String password;
}
