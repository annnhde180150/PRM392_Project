package com.example.homehelperfinder.data.model.request;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request model for updating an existing notification
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationUpdateRequest {

    @SerializedName("title")
    private String title;

    @SerializedName("message")
    private String message;

    @SerializedName("notificationType")
    private String notificationType;

    @SerializedName("isRead")
    private Boolean isRead;

    /**
     * Constructor for updating notification content
     */
    public NotificationUpdateRequest(String title, String message, String notificationType) {
        this.title = title;
        this.message = message;
        this.notificationType = notificationType;
    }

    /**
     * Constructor for marking notification as read/unread
     */
    public NotificationUpdateRequest(Boolean isRead) {
        this.isRead = isRead;
    }

    /**
     * Create request to mark notification as read
     */
    public static NotificationUpdateRequest markAsRead() {
        return new NotificationUpdateRequest(true);
    }

    /**
     * Create request to mark notification as unread
     */
    public static NotificationUpdateRequest markAsUnread() {
        return new NotificationUpdateRequest(false);
    }

    /**
     * Check if this is a read status update only
     */
    public boolean isReadStatusUpdate() {
        return isRead != null && title == null && message == null && notificationType == null;
    }

    /**
     * Check if this is a content update
     */
    public boolean isContentUpdate() {
        return (title != null || message != null || notificationType != null) && isRead == null;
    }

    /**
     * Validate the request
     */
    public boolean isValid() {
        // At least one field should be provided
        return title != null || message != null || notificationType != null || isRead != null;
    }

    @Override
    public String toString() {
        return "NotificationUpdateRequest{" +
                "title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", notificationType='" + notificationType + '\'' +
                ", isRead=" + isRead +
                '}';
    }
}
