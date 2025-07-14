package com.example.homehelperfinder.data.model;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Model for helper skill information
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HelperSkill {
    @SerializedName("helperSkillId")
    private int helperSkillId;
    
    @SerializedName("serviceId")
    private int serviceId;
    
    @SerializedName("yearsOfExperience")
    private int yearsOfExperience;
    
    @SerializedName("isPrimarySkill")
    private boolean isPrimarySkill;
    
    /**
     * Get formatted years of experience
     */
    public String getFormattedExperience() {
        if (yearsOfExperience == 0) {
            return "No experience";
        } else if (yearsOfExperience == 1) {
            return "1 year";
        } else {
            return yearsOfExperience + " years";
        }
    }
    
    /**
     * Get experience level based on years
     */
    public String getExperienceLevel() {
        if (yearsOfExperience == 0) {
            return "Beginner";
        } else if (yearsOfExperience <= 2) {
            return "Junior";
        } else if (yearsOfExperience <= 5) {
            return "Intermediate";
        } else {
            return "Senior";
        }
    }
    
    /**
     * Check if this is a primary skill
     */
    public boolean isPrimary() {
        return isPrimarySkill;
    }
}
