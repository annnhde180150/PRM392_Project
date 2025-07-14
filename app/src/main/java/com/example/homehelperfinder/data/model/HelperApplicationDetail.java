package com.example.homehelperfinder.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Model for detailed helper application information
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HelperApplicationDetail {
    @SerializedName("helperId")
    private int helperId;
    
    @SerializedName("phoneNumber")
    private String phoneNumber;
    
    @SerializedName("email")
    private String email;
    
    @SerializedName("fullName")
    private String fullName;
    
    @SerializedName("profilePictureUrl")
    private String profilePictureUrl;
    
    @SerializedName("bio")
    private String bio;
    
    @SerializedName("dateOfBirth")
    private String dateOfBirth;
    
    @SerializedName("gender")
    private String gender;
    
    @SerializedName("registrationDate")
    private String registrationDate;
    
    @SerializedName("approvalStatus")
    private String approvalStatus;
    
    @SerializedName("approvedByAdminId")
    private Integer approvedByAdminId;
    
    @SerializedName("approvalDate")
    private String approvalDate;
    
    @SerializedName("isActive")
    private boolean isActive;
    
    @SerializedName("documents")
    private List<HelperDocument> documents;
    
    @SerializedName("skills")
    private List<HelperSkill> skills;
    
    @SerializedName("workAreas")
    private List<HelperWorkArea> workAreas;
    
    @SerializedName("totalDocuments")
    private int totalDocuments;
    
    @SerializedName("verifiedDocuments")
    private int verifiedDocuments;
    
    @SerializedName("pendingDocuments")
    private int pendingDocuments;
    
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
    
    /**
     * Check if helper has profile picture
     */
    public boolean hasProfilePicture() {
        return profilePictureUrl != null && !profilePictureUrl.trim().isEmpty();
    }
    
    /**
     * Check if helper has bio
     */
    public boolean hasBio() {
        return bio != null && !bio.trim().isEmpty();
    }
    
    /**
     * Get primary skill
     */
    public HelperSkill getPrimarySkill() {
        if (skills != null) {
            for (HelperSkill skill : skills) {
                if (skill.isPrimarySkill()) {
                    return skill;
                }
            }
        }
        return null;
    }
    
    /**
     * Get document verification percentage
     */
    public double getDocumentVerificationPercentage() {
        if (totalDocuments == 0) return 0.0;
        return (double) verifiedDocuments / totalDocuments * 100.0;
    }
    
    /**
     * Check if all documents are verified
     */
    public boolean areAllDocumentsVerified() {
        return totalDocuments > 0 && verifiedDocuments == totalDocuments;
    }
    
    /**
     * Check if helper has any pending documents
     */
    public boolean hasPendingDocuments() {
        return pendingDocuments > 0;
    }
}
