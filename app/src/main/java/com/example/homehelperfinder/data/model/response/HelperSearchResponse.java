package com.example.homehelperfinder.data.model.response;

import com.example.homehelperfinder.data.model.HelperWorkArea;
import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HelperSearchResponse {
    @SerializedName("helperId")
    private int helperId;

    @SerializedName("helperName")
    private String helperName;

    @SerializedName("serviceName")
    private String serviceName;

    @SerializedName("bio")
    private String bio;

    @SerializedName("rating")
    private Double rating;

    @SerializedName("helperWorkAreas")
    private List<HelperWorkArea> helperWorkAreas;

    @SerializedName("basePrice")
    private Double basePrice;

    @SerializedName("availableStatus")
    private String availableStatus;

    // Helper method for formatted price
    public String getFormattedPrice() {
        if (basePrice == null) return "N/A";
        return String.format("%.0f vnđ", basePrice);
    }

    public String getFormattedRating() {
        if (rating == null) return "0";
        return String.format("%.1f", rating);
    }

    public String getFormattedWorkArea() {
        if (helperWorkAreas != null && !helperWorkAreas.isEmpty()) {
            HelperWorkArea area = helperWorkAreas.get(0);
            return area.getWard() + ", " + area.getDistrict() + ", " + area.getCity();
        }
        return "";
    }
} 