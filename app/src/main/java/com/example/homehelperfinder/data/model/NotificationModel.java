package com.example.homehelperfinder.data.model;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Model class representing a notification in the system
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationModel {

    @SerializedName("notificationId")
    private Long notificationId;

    @SerializedName("recipientUserId")
    private Integer recipientUserId;

    @SerializedName("recipientHelperId")
    private Integer recipientHelperId;

    @SerializedName("title")
    private String title;

    @SerializedName("message")
    private String message;

    @SerializedName("notificationType")
    private String notificationType;

    @SerializedName("referenceId")
    private String referenceId;

    @SerializedName("isRead")
    private Boolean isRead;

    @SerializedName("readTime")
    private String readTime;

    @SerializedName("creationTime")
    private String creationTime;

    @SerializedName("sentTime")
    private String sentTime;

    // Notification types constants
    public static final String TYPE_BOOKING = "BOOKING";
    public static final String TYPE_MESSAGE = "MESSAGE";
    public static final String TYPE_PAYMENT = "PAYMENT";
    public static final String TYPE_SYSTEM = "SYSTEM";
    public static final String TYPE_REMINDER = "REMINDER";
    public static final String TYPE_UPDATE = "UPDATE";

    /**
     * Check if this notification is for a specific user
     */
    public boolean isForUser(Integer userId) {
        return userId != null && userId.equals(recipientUserId);
    }

    /**
     * Check if this notification is for a specific helper
     */
    public boolean isForHelper(Integer helperId) {
        return helperId != null && helperId.equals(recipientHelperId);
    }

    /**
     * Check if this notification is unread
     */
    public boolean isUnread() {
        return isRead == null || !isRead;
    }

    /**
     * Check if this is a booking-related notification
     */
    public boolean isBookingNotification() {
        return TYPE_BOOKING.equals(notificationType);
    }

    /**
     * Check if this is a message notification
     */
    public boolean isMessageNotification() {
        return TYPE_MESSAGE.equals(notificationType);
    }

    /**
     * Check if this is a payment notification
     */
    public boolean isPaymentNotification() {
        return TYPE_PAYMENT.equals(notificationType);
    }

    /**
     * Check if this is a system notification
     */
    public boolean isSystemNotification() {
        return TYPE_SYSTEM.equals(notificationType);
    }

    /**
     * Get reference ID as Long
     */
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

    /**
     * Get reference ID as Integer
     */
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

    @Override
    public String toString() {
        return "NotificationModel{" +
                "notificationId=" + notificationId +
                ", recipientUserId=" + recipientUserId +
                ", recipientHelperId=" + recipientHelperId +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", notificationType='" + notificationType + '\'' +
                ", referenceId='" + referenceId + '\'' +
                ", isRead=" + isRead +
                ", readTime='" + readTime + '\'' +
                ", creationTime='" + creationTime + '\'' +
                ", sentTime='" + sentTime + '\'' +
                '}';
    }
}
