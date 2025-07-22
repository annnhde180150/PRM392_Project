package com.example.homehelperfinder.data.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Response model for helper search results in chat search
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HelperSearchResult {
    
    @SerializedName("helperId")
    private Integer helperId;
    
    @SerializedName("fullName")
    private String fullName;
    
    @SerializedName("email")
    private String email;
    
    @SerializedName("phoneNumber")
    private String phoneNumber;
    
    @SerializedName("profilePictureUrl")
    private String profilePictureUrl;
    
    @SerializedName("bio")
    private String bio;
    
    @SerializedName("isActive")
    private Boolean isActive;
    
    @SerializedName("averageRating")
    private Double averageRating;
    
    @SerializedName("lastLoginDate")
    private String lastLoginDate;
    
    @SerializedName("availableStatus")
    private Integer availableStatus; // 0: Available, 1: Busy, 2: Offline
    
    @SerializedName("hasExistingConversation")
    private Boolean hasExistingConversation;
    
    @SerializedName("lastConversationDate")
    private String lastConversationDate;
    
    @SerializedName("skills")
    private List<String> skills;
    
    /**
     * Get display name for the helper
     */
    public String getDisplayName() {
        return fullName != null && !fullName.trim().isEmpty() ? fullName : email;
    }
    
    /**
     * Check if helper has profile picture
     */
    public boolean hasProfilePicture() {
        return profilePictureUrl != null && !profilePictureUrl.trim().isEmpty();
    }
    
    /**
     * Check if helper is currently active
     */
    public boolean isCurrentlyActive() {
        return isActive != null && isActive;
    }
    
    /**
     * Check if there's an existing conversation with this helper
     */
    public boolean hasConversation() {
        return hasExistingConversation != null && hasExistingConversation;
    }
    
    /**
     * Get availability status text
     */
    public String getAvailabilityStatusText() {
        if (availableStatus == null) return "Unknown";
        switch (availableStatus) {
            case 0: return "Available";
            case 1: return "Busy";
            case 2: return "Offline";
            default: return "Unknown";
        }
    }
    
    /**
     * Get formatted rating
     */
    public String getFormattedRating() {
        if (averageRating == null || averageRating == 0.0) {
            return "No rating";
        }
        return String.format("%.1f", averageRating);
    }
    
    /**
     * Get skills as comma-separated string
     */
    public String getSkillsText() {
        if (skills == null || skills.isEmpty()) {
            return "No skills listed";
        }
        return String.join(", ", skills);
    }
    
    /**
     * Check if helper is available for chat
     */
    public boolean isAvailableForChat() {
        return isCurrentlyActive() && (availableStatus == null || availableStatus == 0);
    }
}
