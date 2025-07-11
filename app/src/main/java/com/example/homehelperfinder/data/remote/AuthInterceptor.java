package com.example.homehelperfinder.data.remote;

import android.content.Context;

import com.example.homehelperfinder.utils.Logger;
import com.example.homehelperfinder.utils.SharedPrefsHelper;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Interceptor to add JWT authentication token to API requests
 */
public class AuthInterceptor implements Interceptor {
    private static final String TAG = "AuthInterceptor";
    private final Context context;

    public AuthInterceptor(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        
        // Get the auth token from SharedPreferences
        SharedPrefsHelper prefsHelper = SharedPrefsHelper.getInstance(context);
        String token = prefsHelper.getAuthToken();
        
        Request.Builder builder = original.newBuilder()
                .header("Content-Type", "application/json");
        
        // Add Authorization header if token exists
        if (token != null && !token.isEmpty()) {
            builder.header("Authorization", "Bearer " + token);
            Logger.d(TAG, "Added Authorization header with token");
        } else {
            Logger.w(TAG, "No auth token found, proceeding without Authorization header");
        }
        
        Request request = builder.build();
        return chain.proceed(request);
    }
}
