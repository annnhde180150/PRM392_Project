package com.example.homehelperfinder.data.model.response;

import com.example.homehelperfinder.data.model.Helper;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Response model for helper login
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HelperLoginResponse {
    @SerializedName("message")
    private String message;
    
    @SerializedName("token")
    private String token;
    
    @SerializedName("helper")
    private Helper helper;
}
