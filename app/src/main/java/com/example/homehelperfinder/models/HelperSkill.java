package com.example.homehelperfinder.models;

public class HelperSkill {
    private String serviceName;
    private Integer yearsOfExperience;
    private boolean isPrimarySkill;

    public HelperSkill(String serviceName, Integer yearsOfExperience, boolean isPrimarySkill) {
        this.serviceName = serviceName;
        this.yearsOfExperience = yearsOfExperience;
        this.isPrimarySkill = isPrimarySkill;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Integer getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(Integer yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public boolean isPrimarySkill() {
        return isPrimarySkill;
    }

    public void setPrimarySkill(boolean primarySkill) {
        isPrimarySkill = primarySkill;
    }
} 