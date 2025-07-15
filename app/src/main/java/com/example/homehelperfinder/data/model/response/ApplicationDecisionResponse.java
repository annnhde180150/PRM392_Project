package com.example.homehelperfinder.data.model.response;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Response model for helper application decision
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationDecisionResponse {
    @SerializedName("message")
    private String message;
    
    /**
     * Check if the response indicates success
     */
    public boolean isSuccess() {
        return message != null && !message.trim().isEmpty();
    }
    
    /**
     * Get formatted message
     */
    public String getFormattedMessage() {
        return message != null ? message : "Decision processed successfully";
    }
}
