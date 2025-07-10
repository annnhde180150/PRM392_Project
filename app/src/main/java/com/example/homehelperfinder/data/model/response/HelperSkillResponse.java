package com.example.homehelperfinder.data.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HelperSkillResponse {
    private String serviceName;
    private Integer yearsOfExperience;
    private boolean isPrimarySkill;
} 