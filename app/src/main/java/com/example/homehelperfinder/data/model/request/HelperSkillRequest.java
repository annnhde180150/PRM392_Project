package com.example.homehelperfinder.data.model.request;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class HelperSkillRequest {
    @SerializedName("serviceId")
    public int serviceId;

    @SerializedName("yearsOfExperience")
    public int yearsOfExperience;

    @SerializedName("isPrimarySkill")
    public boolean isPrimarySkill;

}
