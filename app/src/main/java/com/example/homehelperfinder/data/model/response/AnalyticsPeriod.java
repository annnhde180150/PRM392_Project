package com.example.homehelperfinder.data.model.response;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Model for analytics period information
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnalyticsPeriod {
    
    @SerializedName("start")
    private String start;
    
    @SerializedName("end")
    private String end;
    
    // Utility methods
    public String getFormattedStartDate() {
        return formatDate(start);
    }
    
    public String getFormattedEndDate() {
        return formatDate(end);
    }
    
    public String getFormattedPeriodRange() {
        return getFormattedStartDate() + " - " + getFormattedEndDate();
    }
    
    private String formatDate(String dateString) {
        if (dateString == null) return "";
        
        try {
            // Parse the ISO date format from API
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'", Locale.getDefault());
            Date date = inputFormat.parse(dateString);
            
            // Format to Vietnamese date format
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", new Locale("vi", "VN"));
            return outputFormat.format(date);
        } catch (Exception e) {
            // Fallback to original string if parsing fails
            return dateString;
        }
    }
    
    public String getShortFormattedPeriodRange() {
        return getShortFormattedDate(start) + " - " + getShortFormattedDate(end);
    }
    
    private String getShortFormattedDate(String dateString) {
        if (dateString == null) return "";
        
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'", Locale.getDefault());
            Date date = inputFormat.parse(dateString);
            
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM", new Locale("vi", "VN"));
            return outputFormat.format(date);
        } catch (Exception e) {
            return dateString;
        }
    }
    
    public boolean isValidPeriod() {
        return start != null && end != null && !start.isEmpty() && !end.isEmpty();
    }
}
