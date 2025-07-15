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
public class UserStatusDto {
    
    @SerializedName("userId")
    private Integer userId;
    
    @SerializedName("helperId")
    private Integer helperId;
    
    @SerializedName("userName")
    private String userName;
    
    @SerializedName("userType")
    private String userType;
    
    @SerializedName("status")
    private String status;
    
    @SerializedName("lastSeen")
    private String lastSeen;
    
    @SerializedName("connectionId")
    private String connectionId;
    
    @SerializedName("timestamp")
    private String timestamp;

    // Status constants
    public static final String STATUS_ONLINE = "Online";
    public static final String STATUS_OFFLINE = "Offline";
    public static final String STATUS_AWAY = "Away";
    public static final String STATUS_BUSY = "Busy";

    // User type constants
    public static final String USER_TYPE_USER = "User";
    public static final String USER_TYPE_HELPER = "Helper";

    public boolean isOnline() {
        return STATUS_ONLINE.equals(status);
    }

    public boolean isOffline() {
        return STATUS_OFFLINE.equals(status);
    }

    public boolean isAway() {
        return STATUS_AWAY.equals(status);
    }

    public boolean isBusy() {
        return STATUS_BUSY.equals(status);
    }

    public boolean isUser() {
        return USER_TYPE_USER.equals(userType);
    }

    public boolean isHelper() {
        return USER_TYPE_HELPER.equals(userType);
    }

    public boolean isCurrentUser(Integer currentUserId, Integer currentHelperId) {
        if (currentUserId != null && userId != null) {
            return currentUserId.equals(userId);
        }
        if (currentHelperId != null && helperId != null) {
            return currentHelperId.equals(helperId);
        }
        return false;
    }

    public String getDisplayName() {
        return userName != null ? userName : "Unknown User";
    }

    public String getStatusDisplayText() {
        switch (status) {
            case STATUS_ONLINE:
                return "Online";
            case STATUS_OFFLINE:
                return "Offline";
            case STATUS_AWAY:
                return "Away";
            case STATUS_BUSY:
                return "Busy";
            default:
                return "Unknown";
        }
    }
}
