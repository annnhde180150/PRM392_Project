package com.example.homehelperfinder.data.model;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Helper model for login response
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Helper {
    @SerializedName("id")
    private int id;
    
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
    
    @SerializedName("totalHoursWorked")
    private Double totalHoursWorked;
    
    @SerializedName("averageRating")
    private Double averageRating;
    
    @SerializedName("lastLoginDate")
    private String lastLoginDate;
    
    @SerializedName("role")
    private String role;
}
