package com.example.homehelperfinder.data.model.response;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HelperSkillResponse {
    @SerializedName("helperSkillId")
    private Integer helperSkillId;
    
    @SerializedName("serviceId")
    private Integer serviceId;
    
    @SerializedName("serviceName")
    private String serviceName;
    
    @SerializedName("yearsOfExperience")
    private Integer yearsOfExperience;
    
    @SerializedName("isPrimarySkill")
    private boolean isPrimarySkill;
} 