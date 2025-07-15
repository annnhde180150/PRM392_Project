package com.example.homehelperfinder.data.model.signalr;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDetailsDto {

    @SerializedName("title")
    private String title;

    @SerializedName("message")
    private String message;

    @SerializedName("notificationType")
    private String notificationType;

    @SerializedName("referenceId")
    private String referenceId;

    @SerializedName("timestamp")
    private String timestamp;

    @SerializedName("userId")
    private Integer userId;

    @SerializedName("helperId")
    private Integer helperId;

    @SerializedName("priority")
    private String priority;

    @SerializedName("actionUrl")
    private String actionUrl;

    @SerializedName("isRead")
    private Boolean isRead;

    // Notification types
    public static final String TYPE_CHAT_MESSAGE = "ChatMessage";
    public static final String TYPE_BOOKING_UPDATE = "BookingUpdate";
    public static final String TYPE_SYSTEM_ALERT = "SystemAlert";
    public static final String TYPE_GENERAL = "General";

    // Priority levels
    public static final String PRIORITY_LOW = "Low";
    public static final String PRIORITY_NORMAL = "Normal";
    public static final String PRIORITY_HIGH = "High";
    public static final String PRIORITY_URGENT = "Urgent";

    public boolean isChatMessage() {
        return TYPE_CHAT_MESSAGE.equals(notificationType);
    }

    public boolean isBookingUpdate() {
        return TYPE_BOOKING_UPDATE.equals(notificationType);
    }

    public boolean isSystemAlert() {
        return TYPE_SYSTEM_ALERT.equals(notificationType);
    }

    public boolean isHighPriority() {
        return PRIORITY_HIGH.equals(priority) || PRIORITY_URGENT.equals(priority);
    }

    public boolean isForCurrentUser(Integer currentUserId, Integer currentHelperId) {
        if (currentUserId != null && userId != null) {
            return currentUserId.equals(userId);
        }
        if (currentHelperId != null && helperId != null) {
            return currentHelperId.equals(helperId);
        }
        return false;
    }

    public Long getReferenceIdAsLong() {
        if (referenceId != null && !referenceId.isEmpty()) {
            try {
                return Long.parseLong(referenceId);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    public Integer getReferenceIdAsInteger() {
        if (referenceId != null && !referenceId.isEmpty()) {
            try {
                return Integer.parseInt(referenceId);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
}
