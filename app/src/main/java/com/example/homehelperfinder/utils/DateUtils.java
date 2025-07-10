package com.example.homehelperfinder.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    private static final SimpleDateFormat displayFormat = new SimpleDateFormat(Constants.DATE_FORMAT_DISPLAY, Locale.getDefault());
    private static final SimpleDateFormat apiFormat = new SimpleDateFormat(Constants.DATE_FORMAT_API, Locale.getDefault());
    private static final SimpleDateFormat apiDateTimeFormat = new SimpleDateFormat(Constants.DATETIME_FORMAT_API, Locale.getDefault());

    // Private constructor to prevent instantiation
    private DateUtils() {
        throw new UnsupportedOperationException("DateUtils class cannot be instantiated");
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
