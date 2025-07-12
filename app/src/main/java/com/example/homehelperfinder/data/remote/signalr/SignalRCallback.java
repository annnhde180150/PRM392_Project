package com.example.homehelperfinder.data.remote.signalr;

import com.example.homehelperfinder.data.model.signalr.ChatMessageDto;
import com.example.homehelperfinder.data.model.signalr.NotificationDetailsDto;
import com.example.homehelperfinder.data.model.signalr.UserStatusDto;

/**
 * Interface for SignalR event callbacks
 * Defines methods for handling real-time events from SignalR hub
 */
public interface SignalRCallback {
    
    /**
     * Called when a new chat message is received
     * @param message The received chat message
     */
    void onNewMessage(ChatMessageDto message);
    
    /**
     * Called when a new notification is received
     * @param notification The received notification
     */
    void onNewNotification(NotificationDetailsDto notification);
    
    /**
     * Called when a user's status changes (online/offline/away/busy)
     * @param userStatus The updated user status
     */
    void onUserStatusChanged(UserStatusDto userStatus);
    
    /**
     * Called when successfully connected to SignalR hub
     * @param connectionId The connection ID assigned by the hub
     */
    void onConnected(String connectionId);
    
    /**
     * Called when disconnected from SignalR hub
     * @param reason The reason for disconnection
     */
    void onDisconnected(String reason);
    
    /**
     * Called when connection is being established
     */
    void onConnecting();
    
    /**
     * Called when attempting to reconnect
     * @param attempt The current reconnection attempt number
     */
    void onReconnecting(int attempt);
    
    /**
     * Called when successfully reconnected
     */
    void onReconnected();
    
    /**
     * Called when a SignalR error occurs
     * @param error The error message
     * @param exception The exception that occurred (may be null)
     */
    void onError(String error, Exception exception);
    
    /**
     * Called when successfully joined a conversation
     * @param conversationId The conversation ID that was joined
     */
    void onJoinedConversation(String conversationId);
    
    /**
     * Called when successfully left a conversation
     * @param conversationId The conversation ID that was left
     */
    void onLeftConversation(String conversationId);
    
    /**
     * Called when connection state changes
     * @param oldState The previous connection state
     * @param newState The new connection state
     */
    void onConnectionStateChanged(String oldState, String newState);

    /**
     * Simple implementation of SignalRCallback with empty default methods
     * Extend this class and override only the methods you need
     */
    abstract class SimpleCallback implements SignalRCallback {
        
        @Override
        public void onNewMessage(ChatMessageDto message) {
            // Override if needed
        }
        
        @Override
        public void onNewNotification(NotificationDetailsDto notification) {
            // Override if needed
        }
        
        @Override
        public void onUserStatusChanged(UserStatusDto userStatus) {
            // Override if needed
        }
        
        @Override
        public void onConnected(String connectionId) {
            // Override if needed
        }
        
        @Override
        public void onDisconnected(String reason) {
            // Override if needed
        }
        
        @Override
        public void onConnecting() {
            // Override if needed
        }
        
        @Override
        public void onReconnecting(int attempt) {
            // Override if needed
        }
        
        @Override
        public void onReconnected() {
            // Override if needed
        }
        
        @Override
        public void onError(String error, Exception exception) {
            // Override if needed
        }
        
        @Override
        public void onJoinedConversation(String conversationId) {
            // Override if needed
        }
        
        @Override
        public void onLeftConversation(String conversationId) {
            // Override if needed
        }
        
        @Override
        public void onConnectionStateChanged(String oldState, String newState) {
            // Override if needed
        }
    }
}
