package com.example.homehelperfinder;

import android.app.Application;

import com.example.homehelperfinder.data.remote.RetrofitClient;
import com.example.homehelperfinder.data.remote.signalr.GlobalSignalRCallback;
import com.example.homehelperfinder.data.remote.signalr.SignalRService;
import com.example.homehelperfinder.utils.Logger;
import com.example.homehelperfinder.utils.SharedPrefsHelper;
import com.example.homehelperfinder.utils.signalr.NotificationHelper;
import com.example.homehelperfinder.utils.signalr.SignalRHelper;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import lombok.Getter;

@Getter
public class HomeHelperFinderApplication extends Application {
    private static final String TAG = "HomeHelperFinderApp";

    private GlobalSignalRCallback globalSignalRCallback;

    private NotificationHelper notificationHelper;

    private CompositeDisposable disposables = new CompositeDisposable();

    @Override
    public void onCreate() {
        super.onCreate();

        Logger.d(TAG, "Application starting...");

        // Initialize RetrofitClient with application context
        RetrofitClient.init(this);

        // Initialize SignalR components
        initializeSignalR();

        // Auto-connect SignalR if user is logged in
        autoConnectSignalR();

        Logger.i(TAG, "Application initialized successfully");
    }

    /**
     * Initialize SignalR components
     */
    private void initializeSignalR() {
        try {
            Logger.d(TAG, "Initializing SignalR components...");

            // Create notification helper
            notificationHelper = new NotificationHelper(this);

            // Create global SignalR callback
            globalSignalRCallback = new GlobalSignalRCallback(this);

            // Add global callback to SignalR service
            SignalRService signalRService = SignalRService.getInstance(this);
            signalRService.addCallback(globalSignalRCallback);

            Logger.i(TAG, "SignalR components initialized successfully");

        } catch (Exception e) {
            Logger.e(TAG, "Failed to initialize SignalR components", e);
        }
    }

    /**
     * Auto-connect SignalR if user is logged in
     */
    private void autoConnectSignalR() {
        try {
            SharedPrefsHelper prefsHelper = SharedPrefsHelper.getInstance(this);

            if (prefsHelper.isLoggedIn()) {
                Logger.i(TAG, "User is logged in, auto-connecting SignalR...");

                Disposable disposable = SignalRHelper.initialize(this)
                        .subscribe(
                                () -> Logger.i(TAG, "SignalR auto-connected successfully"),
                                throwable -> Logger.e(TAG, "Failed to auto-connect SignalR", throwable)
                        );
                disposables.add(disposable);
            } else {
                Logger.d(TAG, "User not logged in, skipping SignalR auto-connect");
            }

        } catch (Exception e) {
            Logger.e(TAG, "Error during SignalR auto-connect", e);
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        Logger.d(TAG, "Application terminating...");

        // Shutdown SignalR
        try {
            SignalRHelper.shutdown(this);
            Logger.d(TAG, "SignalR shutdown completed");
        } catch (Exception e) {
            Logger.e(TAG, "Error shutting down SignalR", e);
        }
        disposables.clear();
    }
}
