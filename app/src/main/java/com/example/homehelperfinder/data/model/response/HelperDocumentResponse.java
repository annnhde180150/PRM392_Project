package com.example.homehelperfinder.data.model.response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HelperDocumentResponse {
    private String documentId;
    private String documentType;
    private String documentUrl;
    private String uploadDate;
    private String verificationStatus; // e.g., "Pending", "Approved", "Rejected"
    private int verifiedByAdminId;
    private String verificationDate;
    private String notes;
}
