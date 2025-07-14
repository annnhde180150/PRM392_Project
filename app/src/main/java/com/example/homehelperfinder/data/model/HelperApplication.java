package com.example.homehelperfinder.data.model;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Model for helper application in the applications list
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HelperApplication {
    @SerializedName("helperId")
    private int helperId;
    
    @SerializedName("fullName")
    private String fullName;
    
    @SerializedName("email")
    private String email;
    
    @SerializedName("phoneNumber")
    private String phoneNumber;
    
    @SerializedName("registrationDate")
    private String registrationDate;
    
    @SerializedName("approvalStatus")
    private String approvalStatus;
    
    @SerializedName("documentCount")
    private int documentCount;
    
    @SerializedName("skillCount")
    private int skillCount;
    
    @SerializedName("workAreaCount")
    private int workAreaCount;
    
    @SerializedName("primaryService")
    private String primaryService;
    
    /**
     * Get formatted approval status
     */
    public String getFormattedApprovalStatus() {
        if (approvalStatus == null) return "Unknown";
        return approvalStatus.substring(0, 1).toUpperCase() + approvalStatus.substring(1).toLowerCase();
    }
    
    /**
     * Check if application is pending
     */
    public boolean isPending() {
        return "pending".equalsIgnoreCase(approvalStatus);
    }
    
    /**
     * Check if application is approved
     */
    public boolean isApproved() {
        return "approved".equalsIgnoreCase(approvalStatus);
    }
    
    /**
     * Check if application is rejected
     */
    public boolean isRejected() {
        return "rejected".equalsIgnoreCase(approvalStatus);
    }
    
    /**
     * Check if revision is requested
     */
    public boolean isRevisionRequested() {
        return "revision_requested".equalsIgnoreCase(approvalStatus);
    }
}
