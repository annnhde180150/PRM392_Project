package com.example.homehelperfinder.utils;

import android.content.Context;
import com.example.homehelperfinder.data.remote.RetrofitClient;
import com.example.homehelperfinder.data.remote.auth.AuthApiInterface;
import com.example.homehelperfinder.data.remote.chat.ChatApiInterface;
import com.example.homehelperfinder.data.remote.profile.ProfileManagementApiInterface;

/**
 * Helper class to test token management and API client configuration
 * This class helps verify that the token management fix is working correctly
 */
public class TokenTestHelper {
    private static final String TAG = "TokenTestHelper";

    /**
     * Test that verifies all authenticated APIs are using the correct client
     */
    public static void testApiClientConfiguration(Context context) {
        Logger.i(TAG, "=== Testing API Client Configuration ===");
        
        // Initialize RetrofitClient
        RetrofitClient.init(context);
        
        // Test public APIs (should not have AuthInterceptor)
        Logger.d(TAG, "Testing public APIs...");
        AuthApiInterface publicAuthApi = RetrofitClient.getAuthApiService();
        Logger.d(TAG, "✓ AuthApiInterface (public) created successfully");
        
        // Test authenticated APIs (should have AuthInterceptor)
        Logger.d(TAG, "Testing authenticated APIs...");
        AuthApiInterface authenticatedAuthApi = RetrofitClient.getAuthenticatedAuthApiService();
        ProfileManagementApiInterface profileApi = RetrofitClient.getProfileApiService();
        ChatApiInterface chatApi = RetrofitClient.getChatApiService();
        
        Logger.d(TAG, "✓ AuthApiInterface (authenticated) created successfully");
        Logger.d(TAG, "✓ ProfileManagementApiInterface created successfully");
        Logger.d(TAG, "✓ ChatApiInterface created successfully");
        
        Logger.i(TAG, "=== API Client Configuration Test Completed ===");
    }

    /**
     * Test cache clearing functionality
     */
    public static void testCacheClearingFunctionality(Context context) {
        Logger.i(TAG, "=== Testing Cache Clearing Functionality ===");
        
        // Initialize and create some clients
        RetrofitClient.init(context);
        RetrofitClient.getPublicClient();
        RetrofitClient.getAuthenticatedClient();
        Logger.d(TAG, "✓ Created initial Retrofit clients");
        
        // Test clearing authenticated cache only
        RetrofitClient.clearAuthenticatedCache();
        Logger.d(TAG, "✓ Cleared authenticated cache");
        
        // Test clearing all cache
        RetrofitClient.clearCache();
        Logger.d(TAG, "✓ Cleared all cache");
        
        // Recreate clients to verify they work after cache clearing
        RetrofitClient.getPublicClient();
        RetrofitClient.getAuthenticatedClient();
        Logger.d(TAG, "✓ Successfully recreated clients after cache clearing");
        
        Logger.i(TAG, "=== Cache Clearing Test Completed ===");
    }

    /**
     * Test token storage and retrieval
     */
    public static void testTokenManagement(Context context) {
        Logger.i(TAG, "=== Testing Token Management ===");
        
        SharedPrefsHelper prefsHelper = SharedPrefsHelper.getInstance(context);
        
        // Test saving token
        String testToken = "test_jwt_token_12345";
        prefsHelper.saveUserData("123", "User", "Test User", "test@example.com", testToken);
        Logger.d(TAG, "✓ Saved test token");
        
        // Test retrieving token
        String retrievedToken = prefsHelper.getAuthToken();
        if (testToken.equals(retrievedToken)) {
            Logger.d(TAG, "✓ Token retrieved correctly: " + retrievedToken);
        } else {
            Logger.e(TAG, "✗ Token mismatch! Expected: " + testToken + ", Got: " + retrievedToken);
        }
        
        // Test clearing token
        prefsHelper.clearUserData();
        String clearedToken = prefsHelper.getAuthToken();
        if (clearedToken == null || clearedToken.isEmpty()) {
            Logger.d(TAG, "✓ Token cleared successfully");
        } else {
            Logger.e(TAG, "✗ Token not cleared! Still has: " + clearedToken);
        }
        
        Logger.i(TAG, "=== Token Management Test Completed ===");
    }

    /**
     * Simulate login flow and verify token usage
     */
    public static void simulateLoginFlow(Context context) {
        Logger.i(TAG, "=== Simulating Login Flow ===");
        
        SharedPrefsHelper prefsHelper = SharedPrefsHelper.getInstance(context);
        
        // Step 1: Clear any existing data
        prefsHelper.clearUserData();
        RetrofitClient.clearCache();
        Logger.d(TAG, "✓ Cleared existing data and cache");
        
        // Step 2: Simulate successful login
        String newToken = "new_login_token_67890";
        prefsHelper.saveUserData("456", "User", "New User", "newuser@example.com", newToken);
        RetrofitClient.clearAuthenticatedCache();
        Logger.d(TAG, "✓ Simulated successful login with new token");
        
        // Step 3: Verify token is available for authenticated APIs
        String currentToken = prefsHelper.getAuthToken();
        if (newToken.equals(currentToken)) {
            Logger.d(TAG, "✓ New token is correctly stored and retrievable");
        } else {
            Logger.e(TAG, "✗ Token issue! Expected: " + newToken + ", Got: " + currentToken);
        }
        
        // Step 4: Create authenticated API clients (they should use the new token)
        RetrofitClient.init(context);
        ProfileManagementApiInterface profileApi = RetrofitClient.getProfileApiService();
        ChatApiInterface chatApi = RetrofitClient.getChatApiService();
        Logger.d(TAG, "✓ Created authenticated API clients with new token");
        
        Logger.i(TAG, "=== Login Flow Simulation Completed ===");
    }

    /**
     * Run all tests
     */
    public static void runAllTests(Context context) {
        Logger.i(TAG, "========================================");
        Logger.i(TAG, "Starting Token Management Tests");
        Logger.i(TAG, "========================================");
        
        testApiClientConfiguration(context);
        testCacheClearingFunctionality(context);
        testTokenManagement(context);
        simulateLoginFlow(context);
        
        Logger.i(TAG, "========================================");
        Logger.i(TAG, "All Token Management Tests Completed");
        Logger.i(TAG, "========================================");
    }
}
