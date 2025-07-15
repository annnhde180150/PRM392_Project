package com.example.homehelperfinder.data.remote.signalr;

import android.content.Context;

import com.example.homehelperfinder.data.model.signalr.ChatMessageDto;
import com.example.homehelperfinder.data.model.signalr.NotificationDetailsDto;
import com.example.homehelperfinder.data.model.signalr.UserStatusDto;
import com.example.homehelperfinder.utils.Logger;
import com.example.homehelperfinder.utils.SharedPrefsHelper;
import com.example.homehelperfinder.utils.signalr.NotificationHelper;

/**
 * Global SignalR callback for application-wide event handling
 * Handles notifications, logging, and other global SignalR events
 */
public class GlobalSignalRCallback extends SignalRCallback.SimpleCallback {
    private static final String TAG = "GlobalSignalRCallback";
    
    private final Context context;
    private final NotificationHelper notificationHelper;
    private final SharedPrefsHelper prefsHelper;
    
    public GlobalSignalRCallback(Context context) {
        this.context = context.getApplicationContext();
        this.notificationHelper = new NotificationHelper(this.context);
        this.prefsHelper = SharedPrefsHelper.getInstance(this.context);
    }
    
    @Override
    public void onNewMessage(ChatMessageDto message) {
        Logger.i(TAG, "Global: New chat message received from " + message.getSenderName());
        
        try {
            // Check if message is for current user
            Integer currentUserId = null;
            Integer currentHelperId = null;

            try {
                String userIdStr = prefsHelper.getUserId();
                if (userIdStr != null && !userIdStr.isEmpty()) {
                    currentUserId = Integer.parseInt(userIdStr);
                }
            } catch (NumberFormatException e) {
                Logger.e(TAG, "Error parsing user ID", e);
            }
            
            // Determine if current user is a helper
            String userType = prefsHelper.getUserType();
            if ("helper".equals(userType)) {
                currentHelperId = currentUserId;
                currentUserId = null;
            }
            
            if (message.isForCurrentUser(currentUserId, currentHelperId)) {
                // Show notification for messages directed to current user
                if (shouldShowNotification()) {
                    notificationHelper.showChatMessageNotification(message);
                }
                
                // Log message details
                Logger.d(TAG, "Message details - ID: " + message.getChatId() + 
                        ", Sender: " + message.getSenderName() + 
                        ", Content: " + (message.getMessageContent().length() > 50 ? 
                        message.getMessageContent().substring(0, 50) + "..." : 
                        message.getMessageContent()));
            } else {
                Logger.d(TAG, "Message not for current user, skipping notification");
            }
            
        } catch (Exception e) {
            Logger.e(TAG, "Error handling new message", e);
        }
    }
    
    @Override
    public void onNewNotification(NotificationDetailsDto notification) {
        Logger.i(TAG, "Global: New notification received - " + notification.getTitle());
        
        try {
            // Check if notification is for current user
            Integer currentUserId = null;
            Integer currentHelperId = null;

            try {
                String userIdStr = prefsHelper.getUserId();
                if (userIdStr != null && !userIdStr.isEmpty()) {
                    currentUserId = Integer.parseInt(userIdStr);
                }
            } catch (NumberFormatException e) {
                Logger.e(TAG, "Error parsing user ID", e);
            }
            
            // Determine if current user is a helper
            String userType = prefsHelper.getUserType();
            if ("helper".equals(userType)) {
                currentHelperId = currentUserId;
                currentUserId = null;
            }
            
            if (notification.isForCurrentUser(currentUserId, currentHelperId)) {
                // Show notification
                if (shouldShowNotification()) {
                    notificationHelper.showGeneralNotification(notification);
                }
                
                // Log notification details
                Logger.d(TAG, "Notification details - Type: " + notification.getNotificationType() + 
                        ", Title: " + notification.getTitle() + 
                        ", Priority: " + notification.getPriority());
            } else {
                Logger.d(TAG, "Notification not for current user, skipping");
            }
            
        } catch (Exception e) {
            Logger.e(TAG, "Error handling new notification", e);
        }
    }
    
    @Override
    public void onUserStatusChanged(UserStatusDto userStatus) {
        Logger.d(TAG, "Global: User status changed - " + userStatus.getUserName() + " -> " + userStatus.getStatus());
        
        try {
            // You can implement user status tracking here
            // For example, update a local cache of user statuses
            
            // Optionally show notification for important status changes
            if (userStatus.isOnline() || userStatus.isOffline()) {
                // You might want to show notifications only for specific users
                // notificationHelper.showUserStatusNotification(userStatus.getUserName(), userStatus.getStatus());
            }
            
        } catch (Exception e) {
            Logger.e(TAG, "Error handling user status change", e);
        }
    }
    
    @Override
    public void onConnected(String connectionId) {
        Logger.i(TAG, "Global: SignalR connected with ID: " + connectionId);
        
        try {
            // You can implement connection success handling here
            // For example, sync missed messages or update UI state
            
        } catch (Exception e) {
            Logger.e(TAG, "Error handling connection", e);
        }
    }
    
    @Override
    public void onDisconnected(String reason) {
        Logger.w(TAG, "Global: SignalR disconnected - " + reason);
        
        try {
            // You can implement disconnection handling here
            // For example, show offline indicator or cache messages
            
        } catch (Exception e) {
            Logger.e(TAG, "Error handling disconnection", e);
        }
    }
    
    @Override
    public void onConnecting() {
        Logger.d(TAG, "Global: SignalR connecting...");
    }
    
    @Override
    public void onReconnecting(int attempt) {
        Logger.i(TAG, "Global: SignalR reconnecting (attempt " + attempt + ")");
    }
    
    @Override
    public void onReconnected() {
        Logger.i(TAG, "Global: SignalR reconnected successfully");
        
        try {
            // You can implement reconnection success handling here
            // For example, rejoin conversations or sync data
            
        } catch (Exception e) {
            Logger.e(TAG, "Error handling reconnection", e);
        }
    }
    
    @Override
    public void onError(String error, Exception exception) {
        Logger.e(TAG, "Global: SignalR error - " + error, exception);
        
        try {
            // You can implement error handling here
            // For example, show error notifications or fallback to REST API
            
        } catch (Exception e) {
            Logger.e(TAG, "Error handling SignalR error", e);
        }
    }
    
    @Override
    public void onJoinedConversation(String conversationId) {
        Logger.d(TAG, "Global: Joined conversation - " + conversationId);
    }
    
    @Override
    public void onLeftConversation(String conversationId) {
        Logger.d(TAG, "Global: Left conversation - " + conversationId);
    }
    
    @Override
    public void onConnectionStateChanged(String oldState, String newState) {
        Logger.d(TAG, "Global: Connection state changed from " + oldState + " to " + newState);
        
        try {
            // You can implement state change handling here
            // For example, update UI indicators or notify other components
            
        } catch (Exception e) {
            Logger.e(TAG, "Error handling connection state change", e);
        }
    }
    
    /**
     * Determine if notifications should be shown
     * You can implement custom logic here based on app state, user preferences, etc.
     */
    private boolean shouldShowNotification() {
        try {
            // Check if notifications are enabled
            if (!notificationHelper.areNotificationsEnabled()) {
                return false;
            }
            
            // You can add more conditions here:
            // - Check if app is in foreground
            // - Check user notification preferences
            // - Check do not disturb settings
            // - etc.
            
            return true;
            
        } catch (Exception e) {
            Logger.e(TAG, "Error checking notification conditions", e);
            return false;
        }
    }
    
    /**
     * Get notification helper instance
     */
    public NotificationHelper getNotificationHelper() {
        return notificationHelper;
    }
}
