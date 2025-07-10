package com.example.homehelperfinder.data.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Response model for validation errors (400 Bad Request)
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ValidationErrorResponse {
    @SerializedName("email")
    private List<String> email;
    
    @SerializedName("password")
    private List<String> password;
    
    @SerializedName("username")
    private List<String> username;
    
    // Generic method to get all errors as a map
    public Map<String, List<String>> getAllErrors() {
        Map<String, List<String>> errors = new java.util.HashMap<>();
        if (email != null && !email.isEmpty()) {
            errors.put("email", email);
        }
        if (password != null && !password.isEmpty()) {
            errors.put("password", password);
        }
        if (username != null && !username.isEmpty()) {
            errors.put("username", username);
        }
        return errors;
    }
}
