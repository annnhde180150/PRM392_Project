package com.example.homehelperfinder.utils;

public class Constants {
    
    // Network Configuration
    public static final String BASE_URL = "http://10.0.2.2:5272";
    public static final String API_BASE_PATH = "/api/";
    public static final String RETROFIT_BASE_URL = BASE_URL + API_BASE_PATH;
    public static final String PROFILE_MANAGEMENT_PATH = "ProfileManagement/";
    
    // API Endpoints
    public static final String ENDPOINT_BANNED_PROFILES = "banned";
    public static final String ENDPOINT_ACTIVE_PROFILES = "active";
    
    // Network Timeouts (seconds)
    public static final int CONNECTION_TIMEOUT = 30;
    public static final int READ_TIMEOUT = 30;
    public static final int WRITE_TIMEOUT = 30;
    
    // SharedPreferences Keys
    public static final String PREF_NAME = "HomeHelperFinderPrefs";
    public static final String PREF_USER_ID = "user_id";
    public static final String PREF_USER_TYPE = "user_type";
    public static final String PREF_USER_NAME = "user_name";
    public static final String PREF_USER_EMAIL = "user_email";
    public static final String PREF_IS_LOGGED_IN = "is_logged_in";
    public static final String PREF_AUTH_TOKEN = "auth_token";
    
    // User Types
    public static final String USER_TYPE_CUSTOMER = "customer";
    public static final String USER_TYPE_HELPER = "helper";
    public static final String USER_TYPE_ADMIN = "admin";
    
    // Intent Keys
    public static final String INTENT_USER_TYPE = "user_type";
    public static final String INTENT_USER_ID = "user_id";
    public static final String INTENT_PROFILE_DATA = "profile_data";
    
    // Request Codes
    public static final int REQUEST_CODE_LOGIN = 1001;
    public static final int REQUEST_CODE_PROFILE = 1002;
    public static final int REQUEST_CODE_PERMISSION = 1003;
    
    // Result Codes
    public static final int RESULT_CODE_SUCCESS = 2001;
    public static final int RESULT_CODE_ERROR = 2002;
    public static final int RESULT_CODE_CANCELLED = 2003;
    
    // Error Messages
    public static final String ERROR_NETWORK = "Network error occurred";
    public static final String ERROR_SERVER = "Server error occurred";
    public static final String ERROR_UNKNOWN = "Unknown error occurred";
    public static final String ERROR_NO_DATA = "No data available";
    public static final String ERROR_INVALID_INPUT = "Invalid input provided";
    
    // Success Messages
    public static final String SUCCESS_PROFILE_LOADED = "Profile loaded successfully";
    public static final String SUCCESS_DATA_SAVED = "Data saved successfully";
    public static final String SUCCESS_LOGIN = "Login successful";
    
    // Validation
    public static final int MIN_PASSWORD_LENGTH = 6;
    public static final int MAX_NAME_LENGTH = 50;
    public static final int MAX_EMAIL_LENGTH = 100;
    
    // UI Constants
    public static final int SPLASH_DELAY = 2000; // milliseconds
    public static final int ANIMATION_DURATION = 300; // milliseconds
    
    // Date Formats
    public static final String DATE_FORMAT_DISPLAY = "dd/MM/yyyy";
    public static final String DATE_FORMAT_API = "yyyy-MM-dd";
    public static final String DATETIME_FORMAT_API = "yyyy-MM-dd HH:mm:ss";
    
    // Pagination
    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int MAX_PAGE_SIZE = 100;
    
    // Private constructor to prevent instantiation
    private Constants() {
        throw new UnsupportedOperationException("Constants class cannot be instantiated");
    }
}
