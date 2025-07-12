package com.example.homehelperfinder.utils.signalr;

import android.content.Context;

import com.example.homehelperfinder.data.remote.signalr.SignalRCallback;
import com.example.homehelperfinder.data.remote.signalr.SignalRService;
import com.example.homehelperfinder.utils.Logger;
import com.example.homehelperfinder.utils.SharedPrefsHelper;

import io.reactivex.rxjava3.core.Completable;

/**
 * Helper utility class for SignalR operations
 * Provides convenient static methods for common SignalR tasks
 */
public class SignalRHelper {
    private static final String TAG = "SignalRHelper";
    private static volatile SignalRService serviceInstance;
    private static final Object lock = new Object();
    private static Completable connectionCompletable;


    // Private constructor to prevent instantiation
    private SignalRHelper() {
        throw new UnsupportedOperationException("SignalRHelper class cannot be instantiated");
    }

    /**
     * Get SignalR service instance, ensuring singleton pattern
     */
    public static SignalRService getService(Context context) {
        if (serviceInstance == null) {
            synchronized (lock) {
                if (serviceInstance == null) {
                    serviceInstance = SignalRService.getInstance(context.getApplicationContext());
                }
            }
        }
        return serviceInstance;
    }

    /**
     * Initialize SignalR connection if user is logged in.
     * This method is now idempotent and thread-safe.
     */
    public static Completable initialize(Context context) {
        synchronized (lock) {
            SharedPrefsHelper prefsHelper = SharedPrefsHelper.getInstance(context);

            if (!prefsHelper.isLoggedIn()) {
                Logger.w(TAG, "User not logged in, skipping SignalR initialization");
                return Completable.complete();
            }

            // If a connection is already in progress or completed, return the existing completable
            if (connectionCompletable != null && !getService(context).isDisconnected()) {
                 Logger.d(TAG, "SignalR initialization/connection already in progress or completed.");
                 return connectionCompletable;
            }

            Logger.i(TAG, "Initializing and connecting SignalR for logged-in user");
            SignalRService service = getService(context);

            connectionCompletable = service.initialize()
                    .andThen(service.connect())
                    .doOnComplete(() -> Logger.i(TAG, "SignalR initialized and connected successfully"))
                    .doOnError(throwable -> {
                        Logger.e(TAG, "Failed to initialize and connect SignalR", throwable);
                        // Reset on error to allow retries
                        synchronized (lock) {
                            connectionCompletable = null;
                        }
                    })
                    .cache(); // Cache the result so subscribers don't re-trigger the connection

            return connectionCompletable;
        }
    }
    
    /**
     * Connect to SignalR hub
     */
    public static Completable connect(Context context) {
        SignalRService service = getService(context);
        return service.connect();
    }
    
    /**
     * Disconnect from SignalR hub
     */
    public static Completable disconnect(Context context) {
        SignalRService service = getService(context);
        return service.disconnect();
    }
    
    /**
     * Shutdown SignalR service
     */
    public static Completable shutdown(Context context) {
        Logger.i(TAG, "Shutting down SignalR service");
        synchronized (lock) {
            connectionCompletable = null; // Clear cached connection completable
        }
        SignalRService service = getService(context);
        return service.shutdown();
    }
    
    /**
     * Check if SignalR is connected
     */
    public static boolean isConnected(Context context) {
        SignalRService service = getService(context);
        return service.isConnected();
    }
    
    /**
     * Get current connection state
     */
    public static String getConnectionState(Context context) {
        SignalRService service = getService(context);
        return service.getConnectionState();
    }
    
    /**
     * Get current connection ID
     */
    public static String getConnectionId(Context context) {
        SignalRService service = getService(context);
        return service.getConnectionId();
    }
    
    /**
     * Join a conversation
     */
    public static Completable joinConversation(Context context, String conversationId) {
        SignalRService service = getService(context);
        return service.joinConversation(conversationId);
    }
    
    /**
     * Leave a conversation
     */
    public static Completable leaveConversation(Context context, String conversationId) {
        SignalRService service = getService(context);
        return service.leaveConversation(conversationId);
    }
    
    /**
     * Add a callback for SignalR events
     */
    public static void addCallback(Context context, SignalRCallback callback) {
        SignalRService service = getService(context);
        service.addCallback(callback);
    }
    
    /**
     * Remove a callback
     */
    public static void removeCallback(Context context, SignalRCallback callback) {
        SignalRService service = getService(context);
        service.removeCallback(callback);
    }
    
    /**
     * Initialize SignalR on user login
     */
    public static void onUserLogin(Context context) {
        Logger.i(TAG, "User logged in, initializing SignalR");
        initialize(context)
                .subscribe(
                        () -> Logger.i(TAG, "SignalR initialized successfully after login"),
                        throwable -> Logger.e(TAG, "Failed to initialize SignalR after login", throwable)
                );
    }
    
    /**
     * Shutdown SignalR on user logout
     */
    public static void onUserLogout(Context context) {
        Logger.i(TAG, "User logged out, shutting down SignalR");
        shutdown(context)
                .subscribe(
                        () -> Logger.i(TAG, "SignalR shutdown successfully after logout"),
                        throwable -> Logger.e(TAG, "Failed to shutdown SignalR after logout", throwable)
                );
    }
    
    /**
     * Handle app going to background
     */
    public static void onAppBackground(Context context) {
        Logger.d(TAG, "App going to background");
        // Keep connection alive but could implement background optimizations here
    }
    
    /**
     * Handle app coming to foreground
     */
    public static void onAppForeground(Context context) {
        Logger.d(TAG, "App coming to foreground");
        
        // Reconnect if needed
        if (!isConnected(context)) {
            SharedPrefsHelper prefsHelper = SharedPrefsHelper.getInstance(context);
            if (prefsHelper.isLoggedIn()) {
                Logger.i(TAG, "Reconnecting SignalR on app foreground");
                connect(context)
                        .subscribe(
                                () -> Logger.i(TAG, "SignalR reconnected successfully"),
                                throwable -> Logger.e(TAG, "Failed to reconnect SignalR on foreground", throwable)
                        );
            }
        }
    }
    
    /**
     * Get connection status information
     */
    public static ConnectionStatus getConnectionStatus(Context context) {
        SignalRService service = getService(context);
        return new ConnectionStatus(
                service.isConnected(),
                service.getConnectionState(),
                service.getConnectionId()
        );
    }
    
    /**
     * Connection status information
     */
    public static class ConnectionStatus {
        private final boolean connected;
        private final String state;
        private final String connectionId;
        
        public ConnectionStatus(boolean connected, String state, String connectionId) {
            this.connected = connected;
            this.state = state;
            this.connectionId = connectionId;
        }
        
        public boolean isConnected() { return connected; }
        public String getState() { return state; }
        public String getConnectionId() { return connectionId; }
        
        @Override
        public String toString() {
            return "ConnectionStatus{" +
                    "connected=" + connected +
                    ", state='" + state + '\'' +
                    ", connectionId='" + connectionId + '\'' +
                    '}';
        }
    }
    
    /**
     * Validate SignalR configuration
     */
    public static boolean validateConfiguration(Context context) {
        try {
            SharedPrefsHelper prefsHelper = SharedPrefsHelper.getInstance(context);
            
            // Check if user is logged in
            if (!prefsHelper.isLoggedIn()) {
                Logger.w(TAG, "User not logged in");
                return false;
            }
            
            // Check if auth token is available
            String authToken = prefsHelper.getAuthToken();
            if (authToken == null || authToken.isEmpty()) {
                Logger.w(TAG, "No auth token available");
                return false;
            }
            
            Logger.d(TAG, "SignalR configuration is valid");
            return true;
            
        } catch (Exception e) {
            Logger.e(TAG, "Error validating SignalR configuration", e);
            return false;
        }
    }
}
