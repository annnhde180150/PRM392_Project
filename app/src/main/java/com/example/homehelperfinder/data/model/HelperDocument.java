package com.example.homehelperfinder.data.model;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Model for helper document information
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HelperDocument {
    @SerializedName("documentId")
    private int documentId;
    
    @SerializedName("documentType")
    private String documentType;
    
    @SerializedName("documentUrl")
    private String documentUrl;
    
    @SerializedName("uploadDate")
    private String uploadDate;
    
    @SerializedName("verificationStatus")
    private String verificationStatus;
    
    @SerializedName("verifiedByAdminId")
    private Integer verifiedByAdminId;
    
    @SerializedName("verificationDate")
    private String verificationDate;
    
    @SerializedName("notes")
    private String notes;
    
    /**
     * Check if document is verified
     */
    public boolean isVerified() {
        return "Verified".equalsIgnoreCase(verificationStatus);
    }
    
    /**
     * Check if document is pending verification
     */
    public boolean isPending() {
        return "Pending".equalsIgnoreCase(verificationStatus);
    }
    
    /**
     * Check if document is rejected
     */
    public boolean isRejected() {
        return "Rejected".equalsIgnoreCase(verificationStatus);
    }
    
    /**
     * Get formatted verification status
     */
    public String getFormattedVerificationStatus() {
        if (verificationStatus == null) return "Unknown";
        return verificationStatus.substring(0, 1).toUpperCase() + verificationStatus.substring(1).toLowerCase();
    }
    
    /**
     * Check if document has notes
     */
    public boolean hasNotes() {
        return notes != null && !notes.trim().isEmpty();
    }
}
