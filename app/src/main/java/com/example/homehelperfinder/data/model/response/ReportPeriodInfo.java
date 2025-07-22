package com.example.homehelperfinder.data.model.response;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Model for report period information
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReportPeriodInfo {
    
    @SerializedName("start")
    private String start;
    
    @SerializedName("end")
    private String end;

    /**
     * Get formatted period display
     */
    public String getFormattedPeriod() {
        // You can implement date formatting logic here
        return start + " - " + end;
    }

    /**
     * Check if period is valid
     */
    public boolean isValid() {
        return start != null && end != null && !start.isEmpty() && !end.isEmpty();
    }
}
