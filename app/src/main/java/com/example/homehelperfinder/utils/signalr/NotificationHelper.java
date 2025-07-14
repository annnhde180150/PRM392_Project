package com.example.homehelperfinder.utils.signalr;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.homehelperfinder.R;
import com.example.homehelperfinder.data.model.signalr.ChatMessageDto;
import com.example.homehelperfinder.data.model.signalr.NotificationDetailsDto;
import com.example.homehelperfinder.ui.chat.ChatActivity;
import com.example.homehelperfinder.utils.Constants;
import com.example.homehelperfinder.utils.Logger;

/**
 * Helper class for managing push notifications from SignalR events
 */
public class NotificationHelper {
    private static final String TAG = "NotificationHelper";
    
    private final Context context;
    private final NotificationManagerCompat notificationManager;
    
    public NotificationHelper(Context context) {
        this.context = context.getApplicationContext();
        this.notificationManager = NotificationManagerCompat.from(this.context);
        createNotificationChannels();
    }
    
    /**
     * Create notification channels for Android 8.0+
     */
    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Chat messages channel
            NotificationChannel chatChannel = new NotificationChannel(
                    Constants.NOTIFICATION_CHANNEL_CHAT,
                    "Chat Messages",
                    NotificationManager.IMPORTANCE_HIGH
            );
            chatChannel.setDescription("Notifications for new chat messages");
            chatChannel.enableVibration(true);
            chatChannel.setShowBadge(true);
            
            // General notifications channel
            NotificationChannel generalChannel = new NotificationChannel(
                    Constants.NOTIFICATION_CHANNEL_GENERAL,
                    "General Notifications",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            generalChannel.setDescription("General app notifications");
            generalChannel.enableVibration(true);
            generalChannel.setShowBadge(true);
            
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(chatChannel);
                manager.createNotificationChannel(generalChannel);
                Logger.d(TAG, "Notification channels created");
            }
        }
    }
    
    /**
     * Show notification for new chat message
     */
    @SuppressLint("MissingPermission") // Permission is checked in areNotificationsEnabled()
    public void showChatMessageNotification(ChatMessageDto message) {
        if (message == null) return;
        
        try {
            String title = message.getSenderName() != null ? message.getSenderName() : "New Message";
            String content = message.getMessageContent();
            
            // Create intent to open chat activity
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra(Constants.INTENT_BOOKING_ID, message.getBookingId());
            intent.putExtra(Constants.INTENT_OTHER_USER_ID, message.getSenderUserId());
            intent.putExtra(Constants.INTENT_OTHER_HELPER_ID, message.getSenderHelperId());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            
            PendingIntent pendingIntent = PendingIntent.getActivity(
                    context,
                    message.getChatId().intValue(),
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );
            
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, Constants.NOTIFICATION_CHANNEL_CHAT)
                    .setSmallIcon(R.drawable.ic_notification) // You'll need to add this icon
                    .setContentTitle(title)
                    .setContentText(content)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(content));
            
            // Add sender profile picture if available
            if (message.getSenderProfilePicture() != null && !message.getSenderProfilePicture().isEmpty()) {
                // You could load the image here using Glide or similar
                // builder.setLargeIcon(bitmap);
            }
            
            // Check notification permission before showing notification
            if (areNotificationsEnabled()) {
                notificationManager.notify(Constants.NOTIFICATION_ID_CHAT, builder.build());
                Logger.d(TAG, "Chat message notification shown for message: " + message.getChatId());
            } else {
                Logger.w(TAG, "Notifications not enabled, skipping chat message notification");
            }
            
        } catch (Exception e) {
            Logger.e(TAG, "Error showing chat message notification", e);
        }
    }
    
    /**
     * Show general notification
     */
    @SuppressLint("MissingPermission") // Permission is checked in areNotificationsEnabled()
    public void showGeneralNotification(NotificationDetailsDto notification) {
        if (notification == null) return;
        
        try {
            String title = notification.getTitle() != null ? notification.getTitle() : "Notification";
            String content = notification.getMessage() != null ? notification.getMessage() : "";
            
            // Determine notification priority
            int priority = NotificationCompat.PRIORITY_DEFAULT;
            if (notification.isHighPriority()) {
                priority = NotificationCompat.PRIORITY_HIGH;
            }
            
            // Create intent based on notification type
            Intent intent = createIntentForNotification(notification);
            PendingIntent pendingIntent = null;
            
            if (intent != null) {
                pendingIntent = PendingIntent.getActivity(
                        context,
                        notification.getReferenceIdAsInteger() != null ? notification.getReferenceIdAsInteger() : 0,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
                );
            }
            
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, Constants.NOTIFICATION_CHANNEL_GENERAL)
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setPriority(priority)
                    .setAutoCancel(true)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(content));
            
            if (pendingIntent != null) {
                builder.setContentIntent(pendingIntent);
            }
            
            // Check notification permission before showing notification
            if (areNotificationsEnabled()) {
                notificationManager.notify(Constants.NOTIFICATION_ID_GENERAL, builder.build());
                Logger.d(TAG, "General notification shown: " + title);
            } else {
                Logger.w(TAG, "Notifications not enabled, skipping general notification");
            }
            
        } catch (Exception e) {
            Logger.e(TAG, "Error showing general notification", e);
        }
    }
    
    /**
     * Create appropriate intent for notification type
     */
    private Intent createIntentForNotification(NotificationDetailsDto notification) {
        if (notification.isChatMessage()) {
            // Open chat activity
            Intent intent = new Intent(context, ChatActivity.class);
            Integer referenceId = notification.getReferenceIdAsInteger();
            if (referenceId != null) {
                intent.putExtra(Constants.INTENT_BOOKING_ID, referenceId);
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            return intent;
        }
        
        // For other notification types, you can add specific intents
        // For now, return null to show notification without action
        return null;
    }
    
    /**
     * Cancel all notifications
     */
    public void cancelAllNotifications() {
        notificationManager.cancelAll();
        Logger.d(TAG, "All notifications cancelled");
    }
    
    /**
     * Cancel specific notification
     */
    public void cancelNotification(int notificationId) {
        notificationManager.cancel(notificationId);
        Logger.d(TAG, "Notification cancelled: " + notificationId);
    }
    
    /**
     * Check if notifications are enabled and permission is granted
     */
    public boolean areNotificationsEnabled() {
        // Check if notifications are enabled in system settings
        if (!notificationManager.areNotificationsEnabled()) {
            return false;
        }

        // For Android 13+ (API 33+), check POST_NOTIFICATIONS permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                    == PackageManager.PERMISSION_GRANTED;
        }

        // For older versions, just check if notifications are enabled
        return true;
    }
    
    /**
     * Check if we have notification permission (Android 13+)
     */
    public boolean hasNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                    == PackageManager.PERMISSION_GRANTED;
        }
        return true; // Permission not required for older versions
    }

    /**
     * Get notification manager instance
     */
    public NotificationManagerCompat getNotificationManager() {
        return notificationManager;
    }
    
    /**
     * Show notification for user status change (optional)
     */
    public void showUserStatusNotification(String userName, String status) {
        // This is optional - you might not want to show notifications for every status change
        // Implement if needed for your use case
        Logger.d(TAG, "User status changed: " + userName + " -> " + status);
    }
    
    /**
     * Create notification helper instance
     */
    public static NotificationHelper getInstance(Context context) {
        return new NotificationHelper(context);
    }
}
