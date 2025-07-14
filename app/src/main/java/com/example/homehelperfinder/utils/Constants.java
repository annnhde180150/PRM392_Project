package com.example.homehelperfinder.utils;

public class Constants {

    // Network Configuration
    public static final String BASE_URL = "http://10.0.2.2:5272";
    public static final String API_BASE_PATH = "/api/";
    public static final String RETROFIT_BASE_URL = BASE_URL + API_BASE_PATH;

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

    // Remember Me functionality
    public static final String PREF_REMEMBER_ME = "remember_me";
    public static final String PREF_SAVED_EMAIL = "saved_email";
    public static final String PREF_SAVED_USER_TYPE = "saved_user_type";

    // User Types
    public static final String USER_TYPE_CUSTOMER = "customer";
    public static final String USER_TYPE_HELPER = "helper";
    public static final String USER_TYPE_ADMIN = "admin";

    // Intent Keys
    public static final String INTENT_USER_TYPE = "user_type";
    public static final String INTENT_USER_ID = "user_id";
    public static final String INTENT_PROFILE_DATA = "profile_data";
    public static final String INTENT_BOOKING_ID = "booking_id";
    public static final String INTENT_OTHER_USER_ID = "other_user_id";
    public static final String INTENT_OTHER_HELPER_ID = "other_helper_id";
    public static final String INTENT_CONVERSATION_ID = "conversation_id";

    // Error Messages
    public static final String ERROR_NETWORK = "Network error occurred";
    public static final String ERROR_SERVER = "Server error occurred";
    public static final String ERROR_UNKNOWN = "Unknown error occurred";
    public static final String ERROR_NO_DATA = "No data available";
    public static final String ERROR_INVALID_INPUT = "Invalid input provided";

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

    // Chat Constants
    public static final int MAX_MESSAGE_LENGTH = 2000;
    public static final String MESSAGE_TYPE_TEXT = "text";
    public static final String SENDER_TYPE_USER = "User";
    public static final String SENDER_TYPE_HELPER = "Helper";

    // Pagination
    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int MAX_PAGE_SIZE = 100;

    // SignalR Configuration
    public static final String SIGNALR_HUB_URL = BASE_URL + "/notificationHub";

    // SignalR Event Names
    public static final String SIGNALR_EVENT_CONNECTED = "Connected";
    public static final String SIGNALR_EVENT_RECEIVE_CHAT_MESSAGE = "ReceiveChatMessage";
    public static final String SIGNALR_EVENT_RECEIVE_NOTIFICATION = "ReceiveNotification";
    public static final String SIGNALR_EVENT_USER_STATUS_CHANGED = "UserStatusChanged";
    public static final String SIGNALR_EVENT_ERROR = "Error";

    // SignalR Method Names
    public static final String SIGNALR_METHOD_JOIN_CONVERSATION = "JoinConversation";
    public static final String SIGNALR_METHOD_LEAVE_CONVERSATION = "LeaveConversation";

    // SignalR Connection States
    public static final String SIGNALR_STATE_DISCONNECTED = "Disconnected";
    public static final String SIGNALR_STATE_CONNECTING = "Connecting";
    public static final String SIGNALR_STATE_CONNECTED = "Connected";
    public static final String SIGNALR_STATE_RECONNECTING = "Reconnecting";

    // Notification Configuration
    public static final String NOTIFICATION_CHANNEL_CHAT = "chat_messages";
    public static final String NOTIFICATION_CHANNEL_GENERAL = "general_notifications";
    public static final String NOTIFICATION_CHANNEL_BOOKING = "booking_notifications";
    public static final String NOTIFICATION_CHANNEL_PAYMENT = "payment_notifications";
    public static final int NOTIFICATION_ID_CHAT = 1001;
    public static final int NOTIFICATION_ID_GENERAL = 1002;
    public static final int NOTIFICATION_ID_BOOKING = 1003;
    public static final int NOTIFICATION_ID_PAYMENT = 1004;

    // Notification Intent Keys
    public static final String INTENT_NOTIFICATION_ID = "notification_id";
    public static final String INTENT_NOTIFICATION_TYPE = "notification_type";
    public static final String INTENT_NOTIFICATION_DATA = "notification_data";

    // Private constructor to prevent instantiation
    private Constants() {
        throw new UnsupportedOperationException("Constants class cannot be instantiated");
    }
}
