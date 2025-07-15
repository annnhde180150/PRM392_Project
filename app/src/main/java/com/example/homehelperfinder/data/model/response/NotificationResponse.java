package com.example.homehelperfinder.data.model.response;

import com.example.homehelperfinder.data.model.NotificationModel;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Response model for notification API calls
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponse {

    @SerializedName("success")
    private Boolean success;

    @SerializedName("statusCode")
    private Integer statusCode;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private Object data; // Can be NotificationModel, List<NotificationModel>, or null

    @SerializedName("errors")
    private Object errors;

    @SerializedName("timestamp")
    private String timestamp;

    /**
     * Get single notification from response data
     */
    public NotificationModel getNotification() {
        if (data instanceof NotificationModel) {
            return (NotificationModel) data;
        } else if (data != null) {
            // Handle case where Gson deserializes as LinkedTreeMap or other Map types
            try {
                Gson gson = new Gson();
                String json = gson.toJson(data);
                NotificationModel notification = gson.fromJson(json, NotificationModel.class);
                // Validate that we got a proper notification object
                if (notification != null && notification.getTitle() != null) {
                    return notification;
                }
            } catch (Exception e) {
                // Log the error but don't crash
                android.util.Log.w("NotificationResponse", "Failed to convert data to NotificationModel", e);
            }
        }
        return null;
    }

    /**
     * Get list of notifications from response data
     */
    @SuppressWarnings("unchecked")
    public List<NotificationModel> getNotifications() {
        if (data instanceof List) {
            try {
                // First try direct cast
                List<NotificationModel> directCast = (List<NotificationModel>) data;
                // Validate first item if list is not empty
                if (!directCast.isEmpty() && directCast.get(0) instanceof NotificationModel) {
                    return directCast;
                }
            } catch (ClassCastException e) {
                // Expected when Gson deserializes as List<LinkedTreeMap>
            }

            // Handle case where Gson deserializes as List<LinkedTreeMap> or other Map types
            try {
                Gson gson = new Gson();
                String json = gson.toJson(data);
                Type listType = new TypeToken<List<NotificationModel>>(){}.getType();
                List<NotificationModel> notifications = gson.fromJson(json, listType);
                return notifications != null ? notifications : new ArrayList<>();
            } catch (Exception ex) {
                // Log the error but don't crash
                android.util.Log.w("NotificationResponse", "Failed to convert data to List<NotificationModel>", ex);
                return new ArrayList<>();
            }
        }
        return new ArrayList<>();
    }

    /**
     * Check if the response indicates success
     */
    public boolean isSuccessful() {
        return success != null && success && statusCode != null && statusCode >= 200 && statusCode < 300;
    }

    /**
     * Check if the response has data
     */
    public boolean hasData() {
        return data != null;
    }

    /**
     * Check if the response has errors
     */
    public boolean hasErrors() {
        return errors != null;
    }

    /**
     * Get error message if available
     */
    public String getErrorMessage() {
        if (message != null && !message.isEmpty()) {
            return message;
        }
        if (hasErrors()) {
            return errors.toString();
        }
        return "Unknown error occurred";
    }

    /**
     * Check if this is a not found error
     */
    public boolean isNotFound() {
        return statusCode != null && statusCode == 404;
    }

    /**
     * Check if this is an unauthorized error
     */
    public boolean isUnauthorized() {
        return statusCode != null && (statusCode == 401 || statusCode == 403);
    }

    /**
     * Check if this is a server error
     */
    public boolean isServerError() {
        return statusCode != null && statusCode >= 500;
    }

    /**
     * Check if this is a client error
     */
    public boolean isClientError() {
        return statusCode != null && statusCode >= 400 && statusCode < 500;
    }

    @Override
    public String toString() {
        return "NotificationResponse{" +
                "success=" + success +
                ", statusCode=" + statusCode +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", errors=" + errors +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
