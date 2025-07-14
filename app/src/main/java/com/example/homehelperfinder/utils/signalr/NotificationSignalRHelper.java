package com.example.homehelperfinder.utils.signalr;

import android.content.Context;

import com.example.homehelperfinder.data.model.signalr.NotificationDetailsDto;
import com.example.homehelperfinder.data.remote.signalr.SignalRService;
import com.example.homehelperfinder.utils.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for managing SignalR notification events
 * Provides a centralized way to handle real-time notifications
 */
public class NotificationSignalRHelper {
    private static final String TAG = "NotificationSignalRHelper";
    
    private static NotificationSignalRHelper instance;
    private final SignalRService signalRService;
    private final List<NotificationListener> listeners;
    private SignalRService.NotificationCallback notificationCallback;
    
    /**
     * Interface for notification events
     */
    public interface NotificationListener {
        /**
         * Called when a new notification is received
         */
        void onNotificationReceived(NotificationDetailsDto notification);
        
        /**
         * Called when notification status changes (read/unread)
         */
        default void onNotificationStatusChanged(NotificationDetailsDto notification) {
            // Default implementation - can be overridden
        }
        
        /**
         * Called when notifications are cleared
         */
        default void onNotificationsCleared() {
            // Default implementation - can be overridden
        }
    }
    
    private NotificationSignalRHelper(Context context) {
        this.signalRService = SignalRService.getInstance(context);
        this.listeners = new ArrayList<>();
        setupNotificationCallback();
    }
    
    /**
     * Get singleton instance
     */
    public static synchronized NotificationSignalRHelper getInstance(Context context) {
        if (instance == null) {
            instance = new NotificationSignalRHelper(context.getApplicationContext());
        }
        return instance;
    }
    
    /**
     * Setup SignalR notification callback
     */
    private void setupNotificationCallback() {
        notificationCallback = new SignalRService.NotificationCallback() {
            @Override
            public void onNewNotification(NotificationDetailsDto notification) {
                Logger.d(TAG, "Received new notification: " + notification.getTitle());
                handleNewNotification(notification);
            }

            @Override
            public void onConnected(String connectionId) {
                Logger.d(TAG, "SignalR connected for notifications: " + connectionId);
            }

            @Override
            public void onDisconnected(String reason) {
                Logger.d(TAG, "SignalR disconnected for notifications: " + reason);
            }

            @Override
            public void onError(String error, Exception exception) {
                Logger.e(TAG, "SignalR notification error: " + error, exception);
            }
        };
        
        signalRService.addCallback(notificationCallback);
    }
    
    /**
     * Handle new notification received from SignalR
     */
    private void handleNewNotification(NotificationDetailsDto notification) {
        if (notification == null) {
            Logger.w(TAG, "Received null notification");
            return;
        }
        
        // Notify all listeners
        for (NotificationListener listener : listeners) {
            try {
                listener.onNotificationReceived(notification);
            } catch (Exception e) {
                Logger.e(TAG, "Error notifying listener about new notification", e);
            }
        }
        
        // Show system notification if app is in background
        showSystemNotificationIfNeeded(notification);
    }
    
    /**
     * Show system notification if app is in background
     */
    private void showSystemNotificationIfNeeded(NotificationDetailsDto notification) {
        // This will be handled by the existing NotificationHelper
        // which is already integrated with SignalR
        Logger.d(TAG, "System notification handling delegated to NotificationHelper");
    }
    
    /**
     * Add notification listener
     */
    public void addNotificationListener(NotificationListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
            Logger.d(TAG, "Added notification listener: " + listener.getClass().getSimpleName());
        }
    }
    
    /**
     * Remove notification listener
     */
    public void removeNotificationListener(NotificationListener listener) {
        if (listener != null && listeners.contains(listener)) {
            listeners.remove(listener);
            Logger.d(TAG, "Removed notification listener: " + listener.getClass().getSimpleName());
        }
    }
    
    /**
     * Clear all notification listeners
     */
    public void clearNotificationListeners() {
        listeners.clear();
        Logger.d(TAG, "Cleared all notification listeners");
    }
    
    /**
     * Get number of active listeners
     */
    public int getListenerCount() {
        return listeners.size();
    }
    
    /**
     * Check if SignalR is connected
     */
    public boolean isConnected() {
        return signalRService.isConnected();
    }
    
    /**
     * Get connection state
     */
    public String getConnectionState() {
        return signalRService.getConnectionState();
    }
    
    /**
     * Manually trigger notification for testing
     */
    public void triggerTestNotification() {
        NotificationDetailsDto testNotification = new NotificationDetailsDto();
        testNotification.setTitle("Test Notification");
        testNotification.setMessage("This is a test notification from SignalR");
        testNotification.setNotificationType("SYSTEM");
        testNotification.setTimestamp(String.valueOf(System.currentTimeMillis()));
        
        handleNewNotification(testNotification);
        Logger.d(TAG, "Triggered test notification");
    }
    
    /**
     * Cleanup resources
     */
    public void cleanup() {
        if (notificationCallback != null) {
            signalRService.removeCallback(notificationCallback);
            notificationCallback = null;
        }
        clearNotificationListeners();
        Logger.d(TAG, "Cleaned up NotificationSignalRHelper");
    }
    
    /**
     * Restart notification listening (useful after connection issues)
     */
    public void restart() {
        cleanup();
        setupNotificationCallback();
        Logger.d(TAG, "Restarted notification SignalR helper");
    }
    
    /**
     * Simple notification listener implementation for easy use
     */
    public static abstract class SimpleNotificationListener implements NotificationListener {
        @Override
        public abstract void onNotificationReceived(NotificationDetailsDto notification);
        
        // Other methods have default implementations
    }
}
