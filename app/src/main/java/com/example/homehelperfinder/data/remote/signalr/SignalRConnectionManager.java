package com.example.homehelperfinder.data.remote.signalr;

import android.content.Context;

import com.example.homehelperfinder.data.model.signalr.ChatMessageDto;
import com.example.homehelperfinder.data.model.signalr.NotificationDetailsDto;
import com.example.homehelperfinder.data.model.signalr.UserStatusDto;
import com.example.homehelperfinder.utils.Constants;
import com.example.homehelperfinder.utils.Logger;
import com.example.homehelperfinder.utils.SharedPrefsHelper;
import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;
import com.microsoft.signalr.HubConnectionState;

import java.util.concurrent.CopyOnWriteArrayList;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

/**
 * Manages SignalR connection lifecycle, authentication, and reconnection logic
 * Uses Microsoft SignalR Java Client for real-time communication
 */
public class SignalRConnectionManager {
    private static final String TAG = "SignalRConnectionManager";
    private final SharedPrefsHelper prefsHelper;
    private HubConnection hubConnection;
    private final CopyOnWriteArrayList<SignalRCallback> callbacks;
    private String currentConnectionId;

    public SignalRConnectionManager(Context context) {
        this.prefsHelper = SharedPrefsHelper.getInstance(context);
        this.callbacks = new CopyOnWriteArrayList<>();
    }
    
    /**
     * Initialize the SignalR connection
     */
    public synchronized Completable initialize() {
        if (hubConnection != null && hubConnection.getConnectionState() != HubConnectionState.DISCONNECTED) {
            Logger.d(TAG, "SignalR already initialized");
            return Completable.complete();
        }

        Logger.i(TAG, "Initializing SignalR connection with authentication");

        String authToken = prefsHelper.getAuthToken();
        if (authToken == null || authToken.isEmpty()) {
            Logger.w(TAG, "No auth token available for SignalR connection");
            return Completable.error(new IllegalStateException("No auth token available"));
        }

        try {
            // Build SignalR hub connection with JWT authentication
            hubConnection = HubConnectionBuilder.create(Constants.SIGNALR_HUB_URL)
                    .withAccessTokenProvider(Single.fromCallable(() -> {
                        String token = prefsHelper.getAuthToken();
                        Logger.d(TAG, "Providing auth token for SignalR connection: " + token);
                        return token;
                    }))
                    .build();

            // Setup event handlers
            setupEventHandlers();

            Logger.i(TAG, "SignalR connection initialized successfully");
            return Completable.complete();

        } catch (Exception e) {
            Logger.e(TAG, "Failed to initialize SignalR connection", e);
            return Completable.error(e);
        }
    }

    /**
     * Connect to the SignalR hub
     */
    public Completable connect() {
        if (hubConnection == null) {
            return Completable.error(new IllegalStateException("SignalR not initialized"));
        }

        if (hubConnection.getConnectionState() == HubConnectionState.CONNECTED) {
            Logger.d(TAG, "SignalR already connected");
            return Completable.complete();
        }

        Logger.i(TAG, "Connecting to SignalR hub");

        return Completable.fromAction(() -> {
            try {
                hubConnection.start().blockingAwait(); // Blocking call
                currentConnectionId = hubConnection.getConnectionId();
                Logger.i(TAG, "SignalR connected successfully. Connection ID: " + currentConnectionId);
                notifyConnected(currentConnectionId);
            } catch (Exception e) {
                Logger.e(TAG, "Failed to connect to SignalR hub", e);
                notifyError("Failed to connect to SignalR hub", e);
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Disconnect from the SignalR hub
     */
    public Completable disconnect() {
        if (hubConnection == null || hubConnection.getConnectionState() == HubConnectionState.DISCONNECTED) {
            Logger.d(TAG, "SignalR already disconnected");
            return Completable.complete();
        }

        Logger.i(TAG, "Disconnecting from SignalR hub");

        return Completable.fromAction(() -> {
            try {
                hubConnection.stop().blockingAwait(); // Blocking call
                currentConnectionId = null;
                Logger.i(TAG, "SignalR disconnected successfully");
                notifyDisconnected("Manual disconnect");
            } catch (Exception e) {
                Logger.e(TAG, "Error during SignalR disconnect", e);
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Shutdown the SignalR connection manager
     */
    public synchronized Completable shutdown() {
        Logger.i(TAG, "Shutting down SignalR connection manager");
        callbacks.clear();
        return disconnect();
    }

    /**
     * Check if SignalR is connected
     */
    public boolean isConnected() {
        if (hubConnection == null) {
            return false;
        }
        Logger.d(TAG, "Connection state: " + hubConnection.getConnectionState());
        Logger.d(TAG, "Is connected: " + (hubConnection.getConnectionState() == HubConnectionState.CONNECTED));
        return hubConnection != null && hubConnection.getConnectionState() == HubConnectionState.CONNECTED;
    }

    /**
     * Get current connection state
     */
    public String getConnectionState() {
        if (hubConnection == null) {
            return "DISCONNECTED";
        }
        return hubConnection.getConnectionState().toString();
    }

    /**
     * Join a conversation
     */
    public Completable joinConversation(String conversationId) {
        if (hubConnection == null) {
            return Completable.error(new IllegalStateException("SignalR not initialized."));
        }
        if (conversationId == null) {
            return Completable.error(new IllegalArgumentException("conversationId cannot be null."));
        }
        Logger.d(TAG, "Attempting to join conversation: " + conversationId);

        return Single.fromCallable(() -> {
            if (hubConnection.getConnectionState() != HubConnectionState.CONNECTED) {
                throw new IllegalStateException("SignalR not connected. Current state: " + hubConnection.getConnectionState());
            }
            return hubConnection.invoke(Constants.SIGNALR_METHOD_JOIN_CONVERSATION, conversationId);
        }).flatMapCompletable(completable -> {
            // Convert the HubConnection's Completable to a RxJava3 Completable
            return Completable.create(emitter -> {
                completable.subscribe(
                        emitter::onComplete,
                        emitter::onError
                );
            });
        }).doOnComplete(() -> Logger.d(TAG, "Successfully joined conversation: " + conversationId))
          .doOnError(throwable -> Logger.e(TAG, "Failed to join conversation: " + conversationId, throwable));
    }

    /**
     * Leave a conversation
     */
    public Completable leaveConversation(String conversationId) {
        if (hubConnection == null) {
            return Completable.error(new IllegalStateException("SignalR not initialized."));
        }
        if (conversationId == null) {
            return Completable.complete(); // Or error, depending on desired behavior for null conversationId
        }
        if (hubConnection.getConnectionState() != HubConnectionState.CONNECTED) {
            // Don't try to leave if not connected
            return Completable.complete();
        }
        Logger.d(TAG, "Attempting to leave conversation: " + conversationId);

        return Single.fromCallable(() -> hubConnection.invoke(Constants.SIGNALR_METHOD_LEAVE_CONVERSATION, conversationId))
                .flatMapCompletable(completable -> {
                    // Convert the HubConnection's Completable to a RxJava3 Completable
                    return Completable.create(emitter -> {
                        completable.subscribe(
                                emitter::onComplete,
                                emitter::onError
                        );
                    });
                }).doOnComplete(() -> Logger.d(TAG, "Successfully left conversation: " + conversationId))
                  .doOnError(throwable -> Logger.e(TAG, "Failed to leave conversation: " + conversationId, throwable));
    }

    /**
     * Add a callback for SignalR events
     */
    public void addCallback(SignalRCallback callback) {
        if (callback != null && !callbacks.contains(callback)) {
            callbacks.add(callback);
            Logger.d(TAG, "Added SignalR callback. Total callbacks: " + callbacks.size());
        }
    }

    /**
     * Remove a callback
     */
    public void removeCallback(SignalRCallback callback) {
        if (callback != null && callbacks.remove(callback)) {
            Logger.d(TAG, "Removed SignalR callback. Total callbacks: " + callbacks.size());
        }
    }

    /**
     * Get current connection ID
     */
    public String getConnectionId() {
        return currentConnectionId;
    }

    /**
     * Setup SignalR event handlers
     */
    private void setupEventHandlers() {
        if (hubConnection == null) {
            return;
        }

        // Handle connection events
        hubConnection.onClosed((exception) -> {
            currentConnectionId = null;
            Logger.w(TAG, "SignalR connection closed", exception);
            notifyDisconnected(exception != null ? exception.getMessage() : "Connection closed");
        });

        // Handle chat messages
        hubConnection.on(Constants.SIGNALR_EVENT_RECEIVE_CHAT_MESSAGE, (ChatMessageDto message) -> {
            Logger.d(TAG, "Received chat message: " + message.getMessageContent());
            notifyNewMessage(message);
        }, ChatMessageDto.class);

        // Handle notifications
        hubConnection.on(Constants.SIGNALR_EVENT_RECEIVE_NOTIFICATION, (NotificationDetailsDto notification) -> {
            Logger.d(TAG, "Received notification: " + notification.getTitle());
            notifyNewNotification(notification);
        }, NotificationDetailsDto.class);

        // Handle user status changes
        hubConnection.on(Constants.SIGNALR_EVENT_USER_STATUS_CHANGED, (UserStatusDto userStatus) -> {
            Logger.d(TAG, "User status changed: " + userStatus.getUserId() + " - " + userStatus.getStatus());
            notifyUserStatusChanged(userStatus);
        }, UserStatusDto.class);

        Logger.d(TAG, "SignalR event handlers setup completed");
    }

    /**
     * Notify callbacks about connection
     */
    private void notifyConnected(String connectionId) {
        for (SignalRCallback callback : callbacks) {
            try {
                callback.onConnected(connectionId);
            } catch (Exception e) {
                Logger.e(TAG, "Error in callback onConnected", e);
            }
        }
    }

    /**
     * Notify callbacks about disconnection
     */
    private void notifyDisconnected(String reason) {
        for (SignalRCallback callback : callbacks) {
            try {
                callback.onDisconnected(reason);
            } catch (Exception e) {
                Logger.e(TAG, "Error in callback onDisconnected", e);
            }
        }
    }

    /**
     * Notify callbacks about errors
     */
    private void notifyError(String error, Exception exception) {
        for (SignalRCallback callback : callbacks) {
            try {
                callback.onError(error, exception);
            } catch (Exception e) {
                Logger.e(TAG, "Error in callback onError", e);
            }
        }
    }

    /**
     * Notify callbacks about new messages
     */
    private void notifyNewMessage(ChatMessageDto message) {
        for (SignalRCallback callback : callbacks) {
            try {
                if (callback instanceof SignalRService.ChatCallback) {
                    ((SignalRService.ChatCallback) callback).onNewMessage(message);
                }
            } catch (Exception e) {
                Logger.e(TAG, "Error in callback onNewMessage", e);
            }
        }
    }

    /**
     * Notify callbacks about new notifications
     */
    private void notifyNewNotification(NotificationDetailsDto notification) {
        for (SignalRCallback callback : callbacks) {
            try {
                if (callback instanceof SignalRService.NotificationCallback) {
                    ((SignalRService.NotificationCallback) callback).onNewNotification(notification);
                }
            } catch (Exception e) {
                Logger.e(TAG, "Error in callback onNewNotification", e);
            }
        }
    }

    /**
     * Notify callbacks about user status changes
     */
    private void notifyUserStatusChanged(UserStatusDto userStatus) {
        for (SignalRCallback callback : callbacks) {
            try {
                if (callback instanceof SignalRService.StatusCallback) {
                    ((SignalRService.StatusCallback) callback).onUserStatusChanged(userStatus);
                }
            } catch (Exception e) {
                Logger.e(TAG, "Error in callback onUserStatusChanged", e);
            }
        }
    }

    public boolean isDisconnected() {
        return hubConnection == null || hubConnection.getConnectionState() == HubConnectionState.DISCONNECTED;
    }
}
