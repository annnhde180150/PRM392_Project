package com.example.homehelperfinder.utils;

import android.content.Context;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.HttpException;

public class ErrorHandler {

    // Private constructor to prevent instantiation
    private ErrorHandler() {
        throw new UnsupportedOperationException("ErrorHandler class cannot be instantiated");
    }

    // Handle different types of errors
    public static ErrorResult handleError(Context context, Throwable throwable) {
        Logger.e("ErrorHandler", "Handling error: " + throwable.getMessage(), throwable);

        // Check network connectivity first
        if (!NetworkUtils.isNetworkAvailable(context)) {
            return new ErrorResult("No internet connection available", -1, true);
        }

        // Handle specific error types
        if (throwable instanceof HttpException) {
            return handleHttpException((HttpException) throwable);
        } else if (throwable instanceof ConnectException) {
            return new ErrorResult("Unable to connect to server", -1, true);
        } else if (throwable instanceof SocketTimeoutException) {
            return new ErrorResult("Connection timeout. Please try again", -1, true);
        } else if (throwable instanceof UnknownHostException) {
            return new ErrorResult("Server not found. Please check your connection", -1, true);
        } else {
            return new ErrorResult(Constants.ERROR_UNKNOWN, -1, false);
        }
    }

    // Handle HTTP exceptions
    private static ErrorResult handleHttpException(HttpException httpException) {
        int code = httpException.code();
        String message;

        switch (code) {
            case 400:
                message = "Bad request. Please check your input";
                break;
            case 401:
                message = "Unauthorized. Please login again";
                break;
            case 403:
                message = "Access forbidden";
                break;
            case 404:
                message = "Resource not found";
                break;
            case 500:
                message = "Internal server error";
                break;
            case 502:
                message = "Bad gateway";
                break;
            case 503:
                message = "Service unavailable";
                break;
            default:
                message = "Server error occurred (Code: " + code + ")";
                break;
        }

        return new ErrorResult(message, code, false);
    }

    // Get user-friendly error message
    public static String getUserFriendlyMessage(Context context, Throwable throwable) {
        ErrorResult result = handleError(context, throwable);
        return result.getMessage();
    }

    // Check if error requires login
    public static boolean requiresLogin(Throwable throwable) {
        if (throwable instanceof HttpException) {
            int code = ((HttpException) throwable).code();
            return code == 401; // Unauthorized
        }
        return false;
    }

    // Check if error is retryable
    public static boolean isRetryable(Throwable throwable) {
        if (throwable instanceof HttpException) {
            int code = ((HttpException) throwable).code();
            // Retry for server errors (5xx) and some client errors
            return code >= 500 || code == 408 || code == 429;
        } else if (throwable instanceof ConnectException ||
                throwable instanceof SocketTimeoutException ||
                throwable instanceof UnknownHostException) {
            return true;
        }
        return false;
    }

    // Log error with context
    public static void logError(String tag, String operation, Throwable throwable) {
        String errorMessage = String.format("Error in %s: %s", operation, throwable.getMessage());
        Logger.e(tag, errorMessage, throwable);
    }

    // Log API error
    public static void logApiError(String endpoint, Throwable throwable) {
        Logger.apiError(endpoint, "API call failed", throwable);
    }

    public static class ErrorResult {
        private String message;
        private int code;
        private boolean isNetworkError;

        public ErrorResult(String message, int code, boolean isNetworkError) {
            this.message = message;
            this.code = code;
            this.isNetworkError = isNetworkError;
        }

        public String getMessage() {
            return message;
        }

        public int getCode() {
            return code;
        }

        public boolean isNetworkError() {
            return isNetworkError;
        }
    }
}
