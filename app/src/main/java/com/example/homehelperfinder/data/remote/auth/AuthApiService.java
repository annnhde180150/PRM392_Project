package com.example.homehelperfinder.data.remote.auth;

import android.content.Context;

import com.example.homehelperfinder.data.model.request.HelperRequest;
import com.example.homehelperfinder.data.model.response.ApiResponse;
import com.example.homehelperfinder.data.model.request.LoginRequest;
import com.example.homehelperfinder.data.model.response.AdminLoginResponse;
import com.example.homehelperfinder.data.model.response.ErrorResponse;
import com.example.homehelperfinder.data.model.response.HelperLoginResponse;
import com.example.homehelperfinder.data.model.response.HelperResponse;
import com.example.homehelperfinder.data.model.response.LogoutResponse;
import com.example.homehelperfinder.data.model.response.UserLoginResponse;
import com.example.homehelperfinder.data.model.response.ValidationErrorResponse;
import com.example.homehelperfinder.data.remote.BaseApiService;
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
    private final AuthApiInterface publicApiInterface;  // For login (no auth needed)
    private final AuthApiInterface authenticatedApiInterface;  // For logout (auth needed)
    private final Gson gson;

    public AuthApiService() {
        this.publicApiInterface = RetrofitClient.getAuthApiService();
        this.authenticatedApiInterface = RetrofitClient.getAuthenticatedAuthApiService();
        this.gson = new Gson();
    }



    public void registerHelper(Context context, HelperRequest request , AuthCallback<HelperResponse> callback) {
        if (!NetworkUtils.isNetworkAvailable(context)) {
            callback.onError("No internet connection available", null);
            return;
        }

        Logger.d(TAG, "Starting helper register" );

        Call<ApiResponse<HelperResponse>> call = publicApiInterface.registerHelper(request);
        executeWrappedAuthCall(call, "Helper Register", callback);
    }

    // User login
    public void loginUser(Context context, LoginRequest request, AuthCallback<UserLoginResponse> callback) {
        if (!NetworkUtils.isNetworkAvailable(context)) {
            callback.onError("No internet connection available", null);
            return;
        }

        Logger.d(TAG, "Starting user login for email: " + request.getEmail());

        Call<ApiResponse<UserLoginResponse>> call = publicApiInterface.loginUser(request);
        executeWrappedAuthCall(call, "User Login", callback);
    }

    // Helper login
    public void loginHelper(Context context, LoginRequest request, AuthCallback<HelperLoginResponse> callback) {
        if (!NetworkUtils.isNetworkAvailable(context)) {
            callback.onError("No internet connection available", null);
            return;
        }

        Logger.d(TAG, "Starting helper login for email: " + request.getEmail());

        Call<ApiResponse<HelperLoginResponse>> call = publicApiInterface.loginHelper(request);
        executeWrappedAuthCall(call, "Helper Login", callback);
    }

    // Admin login
    public void loginAdmin(Context context, LoginRequest request, AuthCallback<AdminLoginResponse> callback) {
        if (!NetworkUtils.isNetworkAvailable(context)) {
            callback.onError("No internet connection available", null);
            return;
        }

        Logger.d(TAG, "Starting admin login for username: " + request.getEmail());

        Call<ApiResponse<AdminLoginResponse>> call = publicApiInterface.loginAdmin(request);
        executeWrappedAuthCall(call, "Admin Login", callback);
    }

    // Logout
    public void logout(Context context, AuthCallback<LogoutResponse> callback) {
        if (!NetworkUtils.isNetworkAvailable(context)) {
            callback.onError("No internet connection available", null);
            return;
        }

        Logger.d(TAG, "Starting logout");

        Call<ApiResponse<LogoutResponse>> call = authenticatedApiInterface.logout();
        executeWrappedAuthCall(call, "Logout", callback);
    }

    // Generic method to execute wrapped auth API calls with proper error handling
    private <T> void executeWrappedAuthCall(Call<ApiResponse<T>> call, String operation, AuthCallback<T> callback) {
        call.enqueue(new Callback<ApiResponse<T>>() {
            @Override
            public void onResponse(Call<ApiResponse<T>> call, Response<ApiResponse<T>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<T> apiResponse = response.body();
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        Logger.i(TAG, operation + " successful");
                        callback.onSuccess(apiResponse.getData());
                    } else {
                        String errorMsg = apiResponse.getMessage() != null ? apiResponse.getMessage() : "Login failed";
                        Logger.e(TAG, operation + " failed: " + errorMsg);
                        callback.onError(errorMsg, null);
                    }
                } else {
                    handleWrappedErrorResponse(response, operation, callback);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<T>> call, Throwable t) {
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

    // Handle different types of error responses for wrapped API calls
    private <T> void handleWrappedErrorResponse(Response<ApiResponse<T>> response, String operation, AuthCallback<T> callback) {
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
                }

                // Try to parse as ErrorResponse
                try {
                    ErrorResponse errorResponse = gson.fromJson(errorBodyString, ErrorResponse.class);
                    if (errorResponse.getMessage() != null && !errorResponse.getMessage().isEmpty()) {
                        errorMessage = errorResponse.getMessage();
                    }
                } catch (Exception e) {
                    Logger.w(TAG, "Could not parse error response", e);
                }

                // Set appropriate error messages based on status code
                if (code == 401) {
                    errorMessage = "Invalid credentials. Please check your email and password.";
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
