package com.example.homehelperfinder.data.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Response model for customer spending report API
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerSpendingReportResponse {
    
    @SerializedName("totalSpent")
    private double totalSpent;
    
    @SerializedName("averageSpendingPerBooking")
    private double averageSpendingPerBooking;
    
    @SerializedName("paymentMethods")
    private List<Object> paymentMethods; // Currently empty in API
    
    @SerializedName("spendingTrend")
    private List<SpendingTrendItem> spendingTrend;
    
    @SerializedName("period")
    private ReportPeriodInfo period;

    /**
     * Get formatted total spent
     */
    public String getFormattedTotalSpent() {
        return String.format("%.0f VND", totalSpent);
    }

    /**
     * Get formatted average spending per booking
     */
    public String getFormattedAverageSpending() {
        return String.format("%.0f VND", averageSpendingPerBooking);
    }

    /**
     * Check if there is any spending
     */
    public boolean hasSpending() {
        return totalSpent > 0;
    }

    /**
     * Check if spending trend data is available
     */
    public boolean hasSpendingTrend() {
        return spendingTrend != null && !spendingTrend.isEmpty();
    }

    /**
     * Check if payment methods data is available
     */
    public boolean hasPaymentMethods() {
        return paymentMethods != null && !paymentMethods.isEmpty();
    }

    /**
     * Get total transactions count from spending trend
     */
    public int getTotalTransactions() {
        if (spendingTrend == null) return 0;
        return spendingTrend.stream()
                .mapToInt(SpendingTrendItem::getTransactionCount)
                .sum();
    }

    /**
     * Get latest spending trend item
     */
    public SpendingTrendItem getLatestTrend() {
        if (spendingTrend == null || spendingTrend.isEmpty()) return null;
        return spendingTrend.get(spendingTrend.size() - 1);
    }

    /**
     * Check if period information is available
     */
    public boolean hasPeriod() {
        return period != null && period.isValid();
    }
}
