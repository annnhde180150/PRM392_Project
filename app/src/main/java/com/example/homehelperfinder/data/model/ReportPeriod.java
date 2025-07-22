package com.example.homehelperfinder.data.model;

/**
 * Enum for report period types
 */
public enum ReportPeriod {
    DAY("day"),
    WEEK("week"),
    MONTH("month"),
    QUARTER("quarter"),
    YEAR("year");

    private final String value;

    ReportPeriod(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    /**
     * Get ReportPeriod from string value
     */
    public static ReportPeriod fromValue(String value) {
        for (ReportPeriod period : ReportPeriod.values()) {
            if (period.value.equalsIgnoreCase(value)) {
                return period;
            }
        }
        return MONTH; // Default to month
    }

    /**
     * Get display name for UI
     */
    public String getDisplayName() {
        switch (this) {
            case DAY:
                return "Ngày";
            case WEEK:
                return "Tuần";
            case MONTH:
                return "Tháng";
            case QUARTER:
                return "Quý";
            case YEAR:
                return "Năm";
            default:
                return "Tháng";
        }
    }

    @Override
    public String toString() {
        return value;
    }
}
