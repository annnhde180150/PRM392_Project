package com.example.homehelperfinder.data.model.response;

import com.example.homehelperfinder.data.model.User;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Response model for user login
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginResponse {
    @SerializedName("message")
    private String message;
    
    @SerializedName("token")
    private String token;
    
    @SerializedName("user")
    private User user;
}
