package com.example.homehelperfinder.data.remote.signalr;

import android.content.Context;

import com.example.homehelperfinder.data.model.signalr.ChatMessageDto;
import com.example.homehelperfinder.data.model.signalr.NotificationDetailsDto;
import com.example.homehelperfinder.data.model.signalr.UserStatusDto;
import com.example.homehelperfinder.utils.Logger;

import io.reactivex.rxjava3.core.Completable;

/**
 * High-level service interface for SignalR operations
 * Provides simplified methods for the application to interact with SignalR
 */
public class SignalRService {
    private static final String TAG = "SignalRService";

    private final SignalRConnectionManager connectionManager;
    
    private static SignalRService instance;
    
    private SignalRService(Context context) {
        this.connectionManager = new SignalRConnectionManager(context.getApplicationContext());
    }
    
    /**
     * Get singleton instance of SignalRService
     */
    public static synchronized SignalRService getInstance(Context context) {
        if (instance == null) {
            instance = new SignalRService(context);
        }
        return instance;
    }
    
    /**
     * Initialize SignalR connection
     */
    public Completable initialize() {
        Logger.i(TAG, "Initializing SignalR service");
        return connectionManager.initialize();
    }
    
    /**
     * Connect to SignalR hub
     */
    public Completable connect() {
        Logger.i(TAG, "Connecting SignalR service");
        return connectionManager.connect();
    }
    
    /**
     * Disconnect from SignalR hub
     */
    public Completable disconnect() {
        Logger.i(TAG, "Disconnecting SignalR service");
        return connectionManager.disconnect();
    }
    
    /**
     * Shutdown SignalR service
     */
    public Completable shutdown() {
        Logger.i(TAG, "Shutting down SignalR service");
        return connectionManager.shutdown();
    }
    
    /**
     * Check if SignalR is connected
     */
    public boolean isConnected() {
        return connectionManager.isConnected();
    }
    
    /**
     * Get current connection state
     */
    public String getConnectionState() {
        return connectionManager.getConnectionState();
    }
    
    /**
     * Get current connection ID
     */
    public String getConnectionId() {
        return connectionManager.getConnectionId();
    }
    
    /**
     * Join a conversation
     */
    public Completable joinConversation(String conversationId) {
        Logger.d(TAG, "Joining conversation: " + conversationId);
        return connectionManager.joinConversation(conversationId);
    }
    
    /**
     * Leave a conversation with explicit conversation ID
     */
    public Completable leaveConversation(String conversationId) {
        Logger.d(TAG, "Leaving conversation: " + conversationId);
        return connectionManager.leaveConversation(conversationId);
    }
    
    /**
     * Add a callback for SignalR events
     */
    public void addCallback(SignalRCallback callback) {
        connectionManager.addCallback(callback);
    }
    
    /**
     * Remove a callback
     */
    public void removeCallback(SignalRCallback callback) {
        connectionManager.removeCallback(callback);
    }

    public boolean isDisconnected() {
        return connectionManager.isDisconnected();
    }

    /**
     * Simple callback implementation for easy use
     */
    public static abstract class SimpleCallback extends SignalRCallback.SimpleCallback {
        // Inherits all default implementations from SignalRCallback.SimpleCallback
        // Override only the methods you need
    }
    
    /**
     * Callback specifically for chat messages
     */
    public static abstract class ChatCallback extends SimpleCallback {
        @Override
        public abstract void onNewMessage(ChatMessageDto message);
    }
    
    /**
     * Callback specifically for notifications
     */
    public static abstract class NotificationCallback extends SimpleCallback {
        @Override
        public abstract void onNewNotification(NotificationDetailsDto notification);
    }
    
    /**
     * Callback specifically for connection events
     */
    public static abstract class ConnectionCallback extends SimpleCallback {
        @Override
        public abstract void onConnected(String connectionId);
        
        @Override
        public abstract void onDisconnected(String reason);
    }
    
    /**
     * Callback for user status changes
     */
    public static abstract class StatusCallback extends SimpleCallback {
        @Override
        public abstract void onUserStatusChanged(UserStatusDto userStatus);
    }
    
    /**
     * Comprehensive callback that handles all events
     */
    public static abstract class FullCallback implements SignalRCallback {
        // Must implement all methods from SignalRCallback interface
    }
}
