package com.example.homehelperfinder.utils;

/**
 * Network configuration constants for Retrofit API
 */
public class NetworkConfig {
    // Updated to Android emulator localhost mapping
    public static final String BASE_URL = "http://10.0.2.2:5272";
    public static final String PROFILE_MANAGEMENT_ENDPOINT = "/api/ProfileManagement/";
    
    // Full API base URL for Retrofit
    public static final String RETROFIT_BASE_URL = BASE_URL + PROFILE_MANAGEMENT_ENDPOINT;
    
    // API endpoints (relative to base URL)
    public static final String BANNED_PROFILES_ENDPOINT = "banned";
    public static final String ACTIVE_PROFILES_ENDPOINT = "active";
    
    // Request timeout in seconds
    public static final int CONNECTION_TIMEOUT = 30;
    public static final int READ_TIMEOUT = 30;
    public static final int WRITE_TIMEOUT = 30;
    
    // Logging configuration
    public static final String LOG_TAG = "ProfileAPI";
} 