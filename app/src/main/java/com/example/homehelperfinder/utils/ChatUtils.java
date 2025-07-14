package com.example.homehelperfinder.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Utility class for chat-related operations
 */
public class ChatUtils {
    private static final String TAG = "ChatUtils";

    // Private constructor to prevent instantiation
    private ChatUtils() {
        throw new UnsupportedOperationException("ChatUtils class cannot be instantiated");
    }

    public static Date parseTimestamp(String timestamp) {
        if (timestamp == null || timestamp.isEmpty()) {
            return null;
        }

        // Remove nanoseconds/microseconds: leave only 3 digits after '.'
        if (timestamp.contains(".")) {
            int dotIndex = timestamp.indexOf(".");
            int zIndex = timestamp.indexOf("Z", dotIndex);
            if (zIndex == -1) zIndex = timestamp.length();

            String fraction = timestamp.substring(dotIndex + 1, zIndex);
            if (fraction.length() > 3) {
                fraction = fraction.substring(0, 3);
                timestamp = timestamp.substring(0, dotIndex + 1) + fraction + timestamp.substring(zIndex);
            }
        }

        try {
            SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US);
            iso8601Format.setTimeZone(TimeZone.getTimeZone("UTC"));
            return iso8601Format.parse(timestamp);
        } catch (ParseException e) {
            Logger.e(TAG, "Failed to parse timestamp: " + timestamp);
            return null;
        }
    }

    /**
     * Format Date to display string
     */
    public static String formatTimestampForDisplay(String timestamp) {
        Date date = parseTimestamp(timestamp);
        if (date == null) {
            return "";
        }

        SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return displayFormat.format(date);
    }

    /**
     * Format Date to display string with date
     */
    public static String formatTimestampWithDate(String timestamp) {
        Date date = parseTimestamp(timestamp);
        if (date == null) {
            return "";
        }

        SimpleDateFormat displayFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        return displayFormat.format(date);
    }

    /**
     * Validate message content
     */
    public static boolean isValidMessageContent(String content) {
        return content != null && 
               !content.trim().isEmpty() && 
               content.length() <= Constants.MAX_MESSAGE_LENGTH;
    }

    /**
     * Get relative time string (e.g., "2 minutes ago", "1 hour ago")
     */
    public static String getRelativeTimeString(String timestamp) {
        Date date = parseTimestamp(timestamp);
        if (date == null) {
            return "";
        }

        long now = System.currentTimeMillis();
        long messageTime = date.getTime();
        long diff = now - messageTime;

        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        if (days > 0) {
            return days == 1 ? "1 day ago" : days + " days ago";
        } else if (hours > 0) {
            return hours == 1 ? "1 hour ago" : hours + " hours ago";
        } else if (minutes > 0) {
            return minutes == 1 ? "1 minute ago" : minutes + " minutes ago";
        } else {
            return "Just now";
        }
    }

    /**
     * Check if message is from current user
     */
    public static boolean isMessageFromCurrentUser(Integer senderUserId, Integer senderHelperId, String currentUserType, String currentUserId) {
        if (currentUserId == null || currentUserType == null) {
            return false;
        }

        try {
            int currentId = Integer.parseInt(currentUserId);
            
            if (Constants.USER_TYPE_CUSTOMER.equals(currentUserType)) {
                return senderUserId != null && senderUserId == currentId;
            } else if (Constants.USER_TYPE_HELPER.equals(currentUserType)) {
                return senderHelperId != null && senderHelperId == currentId;
            }
        } catch (NumberFormatException e) {
            Logger.e(TAG, "Failed to parse current user ID: " + currentUserId, e);
        }

        return false;
    }

    /**
     * Get display name for conversation participant
     */
    public static String getParticipantDisplayName(String participantName, String participantType) {
        if (participantName == null || participantName.isEmpty()) {
            return participantType != null ? participantType : "Unknown";
        }
        return participantName;
    }

    /**
     * Truncate message content for preview
     */
    public static String truncateMessage(String content, int maxLength) {
        if (content == null) {
            return "";
        }
        
        if (content.length() <= maxLength) {
            return content;
        }
        
        return content.substring(0, maxLength - 3) + "...";
    }
}
