package com.example.homehelperfinder.data.model.request;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request model for creating a new notification
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationCreateRequest {

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

    /**
     * Constructor for user notifications
     */
    public NotificationCreateRequest(Integer recipientUserId, String title, String message, 
                                   String notificationType, String referenceId) {
        this.recipientUserId = recipientUserId;
        this.title = title;
        this.message = message;
        this.notificationType = notificationType;
        this.referenceId = referenceId;
    }

    /**
     * Constructor for helper notifications
     */
    public static NotificationCreateRequest forHelper(Integer recipientHelperId, String title, 
                                                    String message, String notificationType, 
                                                    String referenceId) {
        NotificationCreateRequest request = new NotificationCreateRequest();
        request.recipientHelperId = recipientHelperId;
        request.title = title;
        request.message = message;
        request.notificationType = notificationType;
        request.referenceId = referenceId;
        return request;
    }

    /**
     * Validate the request
     */
    public boolean isValid() {
        return (title != null && !title.trim().isEmpty()) &&
               (message != null && !message.trim().isEmpty()) &&
               (notificationType != null && !notificationType.trim().isEmpty()) &&
               (recipientUserId != null || recipientHelperId != null);
    }

    @Override
    public String toString() {
        return "NotificationCreateRequest{" +
                "recipientUserId=" + recipientUserId +
                ", recipientHelperId=" + recipientHelperId +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", notificationType='" + notificationType + '\'' +
                ", referenceId='" + referenceId + '\'' +
                '}';
    }
}
