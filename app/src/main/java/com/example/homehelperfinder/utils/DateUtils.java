package com.example.homehelperfinder.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    private static final SimpleDateFormat displayFormat = new SimpleDateFormat(Constants.DATE_FORMAT_DISPLAY, Locale.getDefault());
    private static final SimpleDateFormat displayDateTimeFormat = new SimpleDateFormat(Constants.DATETIME_FORMAT_DISPLAY, Locale.getDefault());
    private static final SimpleDateFormat apiFormat = new SimpleDateFormat(Constants.DATE_FORMAT_API, Locale.getDefault());
    private static final SimpleDateFormat apiDateTimeFormat = new SimpleDateFormat(Constants.DATETIME_FORMAT_API, Locale.getDefault());

    // Private constructor to prevent instantiation
    private DateUtils() {
        throw new UnsupportedOperationException("DateUtils class cannot be instantiated");
    }

    // Convert API DateTime string to display format
    public static String formatDateTimeForDisplay(String apiDateTimeString) {
        if (ValidationUtils.isEmpty(apiDateTimeString)) {
            return "";
        }

        try {
            Date date = apiDateTimeFormat.parse(apiDateTimeString);
            return date != null ? displayDateTimeFormat.format(date) : "";
        } catch (ParseException e) {
            Logger.e("DateUtils", "Error parsing date: " + apiDateTimeString, e);
            return apiDateTimeString; // Return original if parsing fails
        }
    }

    // Convert API date string to display format
    public static String formatDateForDisplay(String apiDateString) {
        if (ValidationUtils.isEmpty(apiDateString)) {
            return "";
        }

        try {
            Date date = apiFormat.parse(apiDateString);
            return date != null ? displayFormat.format(date) : "";
        } catch (ParseException e) {
            Logger.e("DateUtils", "Error parsing date: " + apiDateString, e);
            return apiDateString; // Return original if parsing fails
        }
    }

    // Convert display date string to API format
    public static String formatDateForApi(String displayDateString) {
        if (ValidationUtils.isEmpty(displayDateString)) {
            return "";
        }

        try {
            Date date = displayFormat.parse(displayDateString);
            return date != null ? apiFormat.format(date) : "";
        } catch (ParseException e) {
            Logger.e("DateUtils", "Error parsing display date: " + displayDateString, e);
            return displayDateString; // Return original if parsing fails
        }
    }

    // Convert display DateTime string to API format
    public static String formatDateTimeForApi(String displayDateTimeString) {
        if (ValidationUtils.isEmpty(displayDateTimeString)) {
            return "";
        }

        try {
            Date date = displayDateTimeFormat.parse(displayDateTimeString);
            return date != null ? apiDateTimeFormat.format(date) : "";
        } catch (ParseException e) {
            Logger.e("DateUtils", "Error parsing display date: " + displayDateTimeString, e);
            return displayDateTimeString; // Return original if parsing fails
        }
    }

    // Get current date in display format
    public static String getCurrentDateDisplay() {
        return displayFormat.format(new Date());
    }

    // Get current date in API format
    public static String getCurrentDateApi() {
        return apiFormat.format(new Date());
    }

    // Get current datetime in API format
    public static String getCurrentDateTimeApi() {
        return apiDateTimeFormat.format(new Date());
    }

    // Parse API datetime string to Date object
    public static Date parseApiDateTime(String apiDateTimeString) {
        if (ValidationUtils.isEmpty(apiDateTimeString)) {
            return null;
        }

        try {
            return apiDateTimeFormat.parse(apiDateTimeString);
        } catch (ParseException e) {
            Logger.e("DateUtils", "Error parsing API datetime: " + apiDateTimeString, e);
            return null;
        }
    }

    // Parse API date string to Date object
    public static Date parseApiDate(String apiDateString) {
        if (ValidationUtils.isEmpty(apiDateString)) {
            return null;
        }

        try {
            return apiFormat.parse(apiDateString);
        } catch (ParseException e) {
            Logger.e("DateUtils", "Error parsing API date: " + apiDateString, e);
            return null;
        }
    }

    // Check if date string is valid
    public static boolean isValidDate(String dateString, String format) {
        if (ValidationUtils.isEmpty(dateString) || ValidationUtils.isEmpty(format)) {
            return false;
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
            sdf.setLenient(false);
            sdf.parse(dateString);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * Get relative time span (e.g., "2 hours ago", "just now")
     */
    public static String getRelativeTimeSpan(String dateTimeString) {
        if (ValidationUtils.isEmpty(dateTimeString)) {
            return "Unknown time";
        }

        try {
            // Try different date formats
            Date date = null;

            // Try ISO 8601 format first (common for APIs)
            try {
                SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
                date = isoFormat.parse(dateTimeString);
            } catch (ParseException e) {
                // Try without 'Z'
                try {
                    SimpleDateFormat isoFormat2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
                    date = isoFormat2.parse(dateTimeString);
                } catch (ParseException e2) {
                    // Try API datetime format
                    date = apiDateTimeFormat.parse(dateTimeString);
                }
            }

            if (date == null) {
                return "Unknown time";
            }

            long now = System.currentTimeMillis();
            long time = date.getTime();
            long diff = now - time;

            if (diff < 0) {
                return "In the future";
            }

            // Convert to seconds
            long seconds = diff / 1000;

            if (seconds < 60) {
                return "Just now";
            }

            long minutes = seconds / 60;
            if (minutes < 60) {
                return minutes + (minutes == 1 ? " minute ago" : " minutes ago");
            }

            long hours = minutes / 60;
            if (hours < 24) {
                return hours + (hours == 1 ? " hour ago" : " hours ago");
            }

            long days = hours / 24;
            if (days < 7) {
                return days + (days == 1 ? " day ago" : " days ago");
            }

            long weeks = days / 7;
            if (weeks < 4) {
                return weeks + (weeks == 1 ? " week ago" : " weeks ago");
            }

            long months = days / 30;
            if (months < 12) {
                return months + (months == 1 ? " month ago" : " months ago");
            }

            long years = days / 365;
            return years + (years == 1 ? " year ago" : " years ago");

        } catch (ParseException e) {
            Logger.e("DateUtils", "Error parsing datetime for relative time: " + dateTimeString, e);
            return "Unknown time";
        }
    }

    // Get time ago string (e.g., "2 hours ago")
    public static String getTimeAgo(Date date) {
        if (date == null) {
            return "";
        }

        long now = System.currentTimeMillis();
        long time = date.getTime();
        long diff = now - time;

        if (diff < 60000) { // Less than 1 minute
            return "Just now";
        } else if (diff < 3600000) { // Less than 1 hour
            long minutes = diff / 60000;
            return minutes + " minute" + (minutes > 1 ? "s" : "") + " ago";
        } else if (diff < 86400000) { // Less than 1 day
            long hours = diff / 3600000;
            return hours + " hour" + (hours > 1 ? "s" : "") + " ago";
        } else if (diff < 2592000000L) { // Less than 30 days
            long days = diff / 86400000;
            return days + " day" + (days > 1 ? "s" : "") + " ago";
        } else {
            return displayFormat.format(date);
        }
    }

    // Get time ago string from API datetime string
    public static String getTimeAgo(String apiDateTimeString) {
        Date date = parseApiDateTime(apiDateTimeString);
        return getTimeAgo(date);
    }
}
