package com.example.homehelperfinder.data.remote.auth;

import android.content.Context;

import com.example.homehelperfinder.data.model.request.AdminLoginRequest;
import com.example.homehelperfinder.data.model.request.LoginRequest;
import com.example.homehelperfinder.data.model.response.AdminLoginResponse;
import com.example.homehelperfinder.data.model.response.ErrorResponse;
import com.example.homehelperfinder.data.model.response.HelperLoginResponse;
import com.example.homehelperfinder.data.model.response.LogoutResponse;
import com.example.homehelperfinder.data.model.response.UserLoginResponse;
import com.example.homehelperfinder.data.model.response.ValidationErrorResponse;
import com.example.homehelperfinder.data.remote.RetrofitClient;
import com.example.homehelperfinder.utils.Logger;
import com.example.homehelperfinder.utils.NetworkUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Service class for authentication API operations
 */
public class AuthApiService {
    private static final String TAG = "AuthApiService";
    private final AuthApiInterface apiInterface;
    private final Gson gson;

    public AuthApiService() {
        this.apiInterface = RetrofitClient.getAuthApiService();
        this.gson = new Gson();
    }

    // User login
    public void loginUser(Context context, LoginRequest request, AuthCallback<UserLoginResponse> callback) {
        if (!NetworkUtils.isNetworkAvailable(context)) {
            callback.onError("No internet connection available", null);
            return;
        }

        Logger.d(TAG, "Starting user login for email: " + request.getEmail());
        
        Call<UserLoginResponse> call = apiInterface.loginUser(request);
        executeAuthCall(call, "User Login", callback);
    }

    // Helper login
    public void loginHelper(Context context, LoginRequest request, AuthCallback<HelperLoginResponse> callback) {
        if (!NetworkUtils.isNetworkAvailable(context)) {
            callback.onError("No internet connection available", null);
            return;
        }

        Logger.d(TAG, "Starting helper login for email: " + request.getEmail());
        
        Call<HelperLoginResponse> call = apiInterface.loginHelper(request);
        executeAuthCall(call, "Helper Login", callback);
    }

    // Admin login
    public void loginAdmin(Context context, AdminLoginRequest request, AuthCallback<AdminLoginResponse> callback) {
        if (!NetworkUtils.isNetworkAvailable(context)) {
            callback.onError("No internet connection available", null);
            return;
        }

        Logger.d(TAG, "Starting admin login for username: " + request.getUsername());
        
        Call<AdminLoginResponse> call = apiInterface.loginAdmin(request);
        executeAuthCall(call, "Admin Login", callback);
    }

    // Logout
    public void logout(Context context, AuthCallback<LogoutResponse> callback) {
        if (!NetworkUtils.isNetworkAvailable(context)) {
            callback.onError("No internet connection available", null);
            return;
        }

        Logger.d(TAG, "Starting logout");
        
        Call<LogoutResponse> call = apiInterface.logout();
        executeAuthCall(call, "Logout", callback);
    }

    // Generic method to execute auth API calls with proper error handling
    private <T> void executeAuthCall(Call<T> call, String operation, AuthCallback<T> callback) {
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Logger.i(TAG, operation + " successful");
                    callback.onSuccess(response.body());
                } else {
                    handleErrorResponse(response, operation, callback);
                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                String errorMsg = "Network error during " + operation + ": " + t.getMessage();
                Logger.e(TAG, errorMsg, t);
                callback.onError(errorMsg, t);
            }
        });
    }

    // Handle different types of error responses
    private <T> void handleErrorResponse(Response<T> response, String operation, AuthCallback<T> callback) {
        int code = response.code();
        String errorMessage = "Unknown error occurred";

        try {
            if (response.errorBody() != null) {
                String errorBodyString = response.errorBody().string();
                Logger.e(TAG, operation + " failed with code " + code + ": " + errorBodyString);

                if (code == 400) {
                    // Validation errors
                    try {
                        ValidationErrorResponse validationError = gson.fromJson(errorBodyString, ValidationErrorResponse.class);
                        errorMessage = formatValidationErrors(validationError);
                        callback.onValidationError(validationError);
                        return;
                    } catch (Exception e) {
                        Logger.w(TAG, "Could not parse validation error response", e);
                    }
                } else if (code == 401) {
                    // Unauthorized - invalid credentials
                    try {
                        ErrorResponse errorResponse = gson.fromJson(errorBodyString, ErrorResponse.class);
                        errorMessage = errorResponse.getMessage();
                    } catch (Exception e) {
                        errorMessage = "Invalid credentials";
                    }
                } else if (code >= 500) {
                    errorMessage = "Server error. Please try again later.";
                }
            }
        } catch (IOException e) {
            Logger.e(TAG, "Error reading error response body", e);
        }

        callback.onError(errorMessage, new Exception("HTTP " + code + ": " + errorMessage));
    }

    // Format validation errors into a readable string
    private String formatValidationErrors(ValidationErrorResponse validationError) {
        StringBuilder sb = new StringBuilder();
        
        if (validationError.getEmail() != null && !validationError.getEmail().isEmpty()) {
            sb.append(validationError.getEmail().get(0));
        }
        if (validationError.getPassword() != null && !validationError.getPassword().isEmpty()) {
            if (sb.length() > 0) sb.append("\n");
            sb.append(validationError.getPassword().get(0));
        }
        if (validationError.getUsername() != null && !validationError.getUsername().isEmpty()) {
            if (sb.length() > 0) sb.append("\n");
            sb.append(validationError.getUsername().get(0));
        }
        
        return sb.length() > 0 ? sb.toString() : "Validation error occurred";
    }

    // Callback interface for authentication operations
    public interface AuthCallback<T> {
        void onSuccess(T response);
        void onError(String errorMessage, Throwable throwable);
        
        // Optional method for handling validation errors specifically
        default void onValidationError(ValidationErrorResponse validationError) {
            // Default implementation - can be overridden for specific validation handling
        }
    }
}
