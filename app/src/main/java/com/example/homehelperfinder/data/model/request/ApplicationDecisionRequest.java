package com.example.homehelperfinder.data.model.request;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request model for helper application decision
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationDecisionRequest {
    @SerializedName("status")
    private String status;
    
    @SerializedName("comment")
    private String comment;
    
    /**
     * Create an approval request
     */
    public static ApplicationDecisionRequest approve(String comment) {
        return new ApplicationDecisionRequest("approved", comment);
    }
    
    /**
     * Create an approval request without comment
     */
    public static ApplicationDecisionRequest approve() {
        return new ApplicationDecisionRequest("approved", null);
    }
    
    /**
     * Create a rejection request
     */
    public static ApplicationDecisionRequest reject(String comment) {
        return new ApplicationDecisionRequest("rejected", comment);
    }
    
    /**
     * Create a revision request
     */
    public static ApplicationDecisionRequest requestRevision(String comment) {
        return new ApplicationDecisionRequest("revision_requested", comment);
    }
    
    /**
     * Check if this is an approval decision
     */
    public boolean isApproval() {
        return "approved".equals(status);
    }
    
    /**
     * Check if this is a rejection decision
     */
    public boolean isRejection() {
        return "rejected".equals(status);
    }
    
    /**
     * Check if this is a revision request
     */
    public boolean isRevisionRequest() {
        return "revision_requested".equals(status);
    }
    
    /**
     * Check if comment is provided
     */
    public boolean hasComment() {
        return comment != null && !comment.trim().isEmpty();
    }
    
    /**
     * Validate the request
     */
    public boolean isValid() {
        return status != null && 
               (status.equals("approved") || status.equals("rejected") || status.equals("revision_requested"));
    }
}
