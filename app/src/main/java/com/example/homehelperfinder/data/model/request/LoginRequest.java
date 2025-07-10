package com.example.homehelperfinder.data.model.request;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request model for user and helper login
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @SerializedName("email")
    private String email;
    
    @SerializedName("password")
    private String password;
}
