package com.example.homehelperfinder.utils;

import android.util.Log;

public class Logger {

    private static final String DEFAULT_TAG = "HomeHelperFinder";
    private static final boolean DEBUG = true;

    // Private constructor to prevent instantiation
    private Logger() {
        throw new UnsupportedOperationException("Logger class cannot be instantiated");
    }

    // Debug logs
    public static void d(String message) {
        d(DEFAULT_TAG, message);
    }

    public static void d(String tag, String message) {
        if (DEBUG) {
            Log.d(tag, message);
        }
    }

    // Info logs
    public static void i(String message) {
        i(DEFAULT_TAG, message);
    }

    public static void i(String tag, String message) {
        if (DEBUG) {
            Log.i(tag, message);
        }
    }

    // Warning logs
    public static void w(String message) {
        w(DEFAULT_TAG, message);
    }

    public static void w(String tag, String message) {
        if (DEBUG) {
            Log.w(tag, message);
        }
    }

    public static void w(String tag, String message, Throwable throwable) {
        if (DEBUG) {
            Log.w(tag, message, throwable);
        }
    }

    // Error logs
    public static void e(String message) {
        e(DEFAULT_TAG, message);
    }

    public static void e(String tag, String message) {
        Log.e(tag, message); // Always log errors, even in release
    }

    public static void e(String tag, String message, Throwable throwable) {
        Log.e(tag, message, throwable); // Always log errors, even in release
    }

    // Verbose logs
    public static void v(String message) {
        v(DEFAULT_TAG, message);
    }

    public static void v(String tag, String message) {
        if (DEBUG) {
            Log.v(tag, message);
        }
    }

    // Network logs
    public static void network(String message) {
        d("Network", message);
    }

    public static void networkError(String message, Throwable throwable) {
        e("Network", message, throwable);
    }

    // API logs
    public static void api(String endpoint, String message) {
        d("API_" + endpoint, message);
    }

    public static void apiError(String endpoint, String message, Throwable throwable) {
        e("API_" + endpoint, message, throwable);
    }

    // UI logs
    public static void ui(String activity, String message) {
        d("UI_" + activity, message);
    }

    // Database logs
    public static void db(String message) {
        d("Database", message);
    }

    public static void dbError(String message, Throwable throwable) {
        e("Database", message, throwable);
    }

    // Method entry/exit logs
    public static void methodEntry(String className, String methodName) {
        d(className, ">>> " + methodName);
    }

    public static void methodExit(String className, String methodName) {
        d(className, "<<< " + methodName);
    }
}
