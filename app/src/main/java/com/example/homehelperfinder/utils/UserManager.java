package com.example.homehelperfinder.utils;

import android.content.Context;

/**
 * Utility class to manage current user information
 */
public class UserManager {
    
    private static UserManager instance;
    private final SharedPrefsHelper sharedPrefsHelper;
    
    private UserManager(Context context) {
        this.sharedPrefsHelper = SharedPrefsHelper.getInstance(context);
    }
    
    public static synchronized UserManager getInstance(Context context) {
        if (instance == null) {
            instance = new UserManager(context.getApplicationContext());
        }
        return instance;
    }
    
    /**
     * Get current user ID
     * @return User ID as integer, or -1 if not found/invalid
     */
    public int getCurrentUserId() {
        String userIdStr = sharedPrefsHelper.getUserId();
        if (userIdStr != null && !userIdStr.isEmpty()) {
            try {
                return Integer.parseInt(userIdStr);
            } catch (NumberFormatException e) {
                Logger.e("UserManager", "Error parsing user ID: " + userIdStr, e);
                return -1;
            }
        }
        Logger.w("UserManager", "No user ID found in SharedPreferences");
        return -1;
    }
    
    /**
     * Get current user type
     * @return User type string (customer, helper, admin) or empty string if not found
     */
    public String getCurrentUserType() {
        return sharedPrefsHelper.getUserType();
    }
    
    /**
     * Get current user name
     * @return User name or empty string if not found
     */
    public String getCurrentUserName() {
        return sharedPrefsHelper.getUserName();
    }
    
    /**
     * Get current user email
     * @return User email or empty string if not found
     */
    public String getCurrentUserEmail() {
        return sharedPrefsHelper.getUserEmail();
    }
    
    /**
     * Get current auth token
     * @return Auth token or empty string if not found
     */
    public String getCurrentAuthToken() {
        return sharedPrefsHelper.getAuthToken();
    }
    
    /**
     * Check if user is logged in
     * @return true if logged in, false otherwise
     */
    public boolean isLoggedIn() {
        return sharedPrefsHelper.isLoggedIn();
    }
    
    /**
     * Check if current user is a customer
     * @return true if customer, false otherwise
     */
    public boolean isCustomer() {
        return Constants.USER_TYPE_CUSTOMER.equals(getCurrentUserType());
    }
    
    /**
     * Check if current user is a helper
     * @return true if helper, false otherwise
     */
    public boolean isHelper() {
        return Constants.USER_TYPE_HELPER.equals(getCurrentUserType());
    }
    
    /**
     * Check if current user is an admin
     * @return true if admin, false otherwise
     */
    public boolean isAdmin() {
        return Constants.USER_TYPE_ADMIN.equals(getCurrentUserType());
    }
    
    /**
     * Clear all user data (logout)
     */
    public void logout() {
        sharedPrefsHelper.clearUserData();
        Logger.i("UserManager", "User logged out");
    }
} 