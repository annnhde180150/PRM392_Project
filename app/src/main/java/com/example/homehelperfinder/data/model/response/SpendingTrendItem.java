package com.example.homehelperfinder.data.model.response;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Model for spending trend data in customer reports
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SpendingTrendItem {
    
    @SerializedName("year")
    private int year;
    
    @SerializedName("month")
    private int month;
    
    @SerializedName("monthName")
    private String monthName;
    
    @SerializedName("revenue")
    private double revenue;
    
    @SerializedName("platformFees")
    private double platformFees;
    
    @SerializedName("helperEarnings")
    private double helperEarnings;
    
    @SerializedName("transactionCount")
    private int transactionCount;
    
    @SerializedName("growthRate")
    private double growthRate;

    /**
     * Get formatted revenue
     */
    public String getFormattedRevenue() {
        return String.format("%.0f VND", revenue);
    }

    /**
     * Get formatted platform fees
     */
    public String getFormattedPlatformFees() {
        return String.format("%.0f VND", platformFees);
    }

    /**
     * Get formatted helper earnings
     */
    public String getFormattedHelperEarnings() {
        return String.format("%.0f VND", helperEarnings);
    }

    /**
     * Get formatted growth rate as percentage
     */
    public String getFormattedGrowthRate() {
        if (growthRate > 0) {
            return String.format("+%.1f%%", growthRate * 100);
        } else if (growthRate < 0) {
            return String.format("%.1f%%", growthRate * 100);
        } else {
            return "0%";
        }
    }

    /**
     * Get period display text
     */
    public String getPeriodDisplay() {
        return monthName + " " + year;
    }

    /**
     * Check if there are any transactions
     */
    public boolean hasTransactions() {
        return transactionCount > 0;
    }

    /**
     * Check if there is revenue
     */
    public boolean hasRevenue() {
        return revenue > 0;
    }

    /**
     * Check if growth rate is positive
     */
    public boolean hasPositiveGrowth() {
        return growthRate > 0;
    }
}
