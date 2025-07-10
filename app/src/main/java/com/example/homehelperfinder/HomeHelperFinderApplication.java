package com.example.homehelperfinder;

import android.app.Application;

import com.example.homehelperfinder.data.remote.RetrofitClient;
import com.example.homehelperfinder.utils.Logger;

/**
 * Application class for global initialization
 */
public class HomeHelperFinderApplication extends Application {
    private static final String TAG = "HomeHelperFinderApp";

    @Override
    public void onCreate() {
        super.onCreate();
        
        Logger.d(TAG, "Application starting...");
        
        // Initialize RetrofitClient with application context
        RetrofitClient.init(this);
        
        Logger.i(TAG, "Application initialized successfully");
    }
}
