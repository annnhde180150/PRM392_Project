package com.example.homehelperfinder.data.model.response;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Response model for user search results in chat search
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserSearchResult {
    
    @SerializedName("userId")
    private Integer userId;
    
    @SerializedName("fullName")
    private String fullName;
    
    @SerializedName("email")
    private String email;
    
    @SerializedName("phoneNumber")
    private String phoneNumber;
    
    @SerializedName("profilePictureUrl")
    private String profilePictureUrl;
    
    @SerializedName("isActive")
    private Boolean isActive;
    
    @SerializedName("lastLoginDate")
    private String lastLoginDate;
    
    @SerializedName("hasExistingConversation")
    private Boolean hasExistingConversation;
    
    @SerializedName("lastConversationDate")
    private String lastConversationDate;
    
    /**
     * Get display name for the user
     */
    public String getDisplayName() {
        return fullName != null && !fullName.trim().isEmpty() ? fullName : email;
    }
    
    /**
     * Check if user has profile picture
     */
    public boolean hasProfilePicture() {
        return profilePictureUrl != null && !profilePictureUrl.trim().isEmpty();
    }
    
    /**
     * Check if user is currently active
     */
    public boolean isCurrentlyActive() {
        return isActive != null && isActive;
    }
    
    /**
     * Check if there's an existing conversation with this user
     */
    public boolean hasConversation() {
        return hasExistingConversation != null && hasExistingConversation;
    }
}
