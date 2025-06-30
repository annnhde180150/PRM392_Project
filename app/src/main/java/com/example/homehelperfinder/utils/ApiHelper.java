package com.example.homehelperfinder.utils;

import android.content.Context;
import com.example.homehelperfinder.data.remote.BaseApiService;

public class ApiHelper {
    
    // Quick success callback
    public static <T> BaseApiService.ApiCallback<T> onSuccess(OnSuccessCallback<T> callback) {
        return new BaseApiService.ApiCallback<T>() {
            @Override
            public void onSuccess(T data) {
                if (callback != null) {
                    callback.onSuccess(data);
                }
            }
            
            @Override
            public void onError(String errorMessage, Throwable throwable) {
                Logger.e("ApiHelper", "API Error: " + errorMessage, throwable);
            }
        };
    }
    
    // Full callback with error handling
    public static <T> BaseApiService.ApiCallback<T> callback(
            OnSuccessCallback<T> onSuccess, 
            OnErrorCallback onError) {
        return new BaseApiService.ApiCallback<T>() {
            @Override
            public void onSuccess(T data) {
                if (onSuccess != null) {
                    onSuccess.onSuccess(data);
                }
            }
            
            @Override
            public void onError(String errorMessage, Throwable throwable) {
                if (onError != null) {
                    onError.onError(errorMessage, throwable);
                } else {
                    Logger.e("ApiHelper", "API Error: " + errorMessage, throwable);
                }
            }
        };
    }
    
    // Callback with context for UI operations
    public static <T> BaseApiService.ApiCallback<T> callbackWithContext(
            Context context,
            OnSuccessCallback<T> onSuccess, 
            OnErrorCallback onError) {
        return new BaseApiService.ApiCallback<T>() {
            @Override
            public void onSuccess(T data) {
                if (onSuccess != null) {
                    onSuccess.onSuccess(data);
                }
            }
            
            @Override
            public void onError(String errorMessage, Throwable throwable) {
                if (onError != null) {
                    onError.onError(errorMessage, throwable);
                } else {
                    // Default error handling
                    Logger.e("ApiHelper", "API Error: " + errorMessage, throwable);
                    if (context != null) {
                        // Could show toast or handle UI error here
                    }
                }
            }
        };
    }
    
    // Simple interfaces for callbacks
    public interface OnSuccessCallback<T> {
        void onSuccess(T data);
    }
    
    public interface OnErrorCallback {
        void onError(String errorMessage, Throwable throwable);
    }
    
    // Private constructor to prevent instantiation
    private ApiHelper() {
        throw new UnsupportedOperationException("ApiHelper class cannot be instantiated");
    }
}
