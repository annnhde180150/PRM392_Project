package com.example.homehelperfinder.data.model.response;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Response model for logout
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LogoutResponse {
    @SerializedName("message")
    private String message;
}
