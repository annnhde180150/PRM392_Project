package com.example.homehelperfinder.utils;

import android.content.Context;
import android.content.Intent;

import com.example.homehelperfinder.ui.chat.ChatActivity;
import com.example.homehelperfinder.ui.chat.ConversationsActivity;

/**
 * Helper class for chat-related navigation and utilities
 */
public class ChatHelper {
    private static final String TAG = "ChatHelper";

    // Private constructor to prevent instantiation
    private ChatHelper() {
        throw new UnsupportedOperationException("ChatHelper class cannot be instantiated");
    }

    /**
     * Start a chat with a specific user
     */
    public static void startChatWithUser(Context context, int userId, String userName) {
        startChatWithUser(context, userId, userName, null);
    }

    /**
     * Start a chat with a specific user for a booking
     */
    public static void startChatWithUser(Context context, int userId, String userName, Integer bookingId) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(Constants.INTENT_OTHER_USER_ID, userId);
        intent.putExtra("participant_name", userName);
        
        if (bookingId != null) {
            intent.putExtra(Constants.INTENT_BOOKING_ID, bookingId);
        }
        
        context.startActivity(intent);
        Logger.d(TAG, "Starting chat with user: " + userName + " (ID: " + userId + ")");
    }

    /**
     * Start a chat with a specific helper
     */
    public static void startChatWithHelper(Context context, int helperId, String helperName) {
        startChatWithHelper(context, helperId, helperName, null);
    }

    /**
     * Start a chat with a specific helper for a booking
     */
    public static void startChatWithHelper(Context context, int helperId, String helperName, Integer bookingId) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(Constants.INTENT_OTHER_HELPER_ID, helperId);
        intent.putExtra("participant_name", helperName);
        
        if (bookingId != null) {
            intent.putExtra(Constants.INTENT_BOOKING_ID, bookingId);
        }
        
        context.startActivity(intent);
        Logger.d(TAG, "Starting chat with helper: " + helperName + " (ID: " + helperId + ")");
    }

    /**
     * Start a chat for a specific booking
     */
    public static void startBookingChat(Context context, int bookingId, int otherParticipantId, 
                                       String participantName, boolean isHelper) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(Constants.INTENT_BOOKING_ID, bookingId);
        intent.putExtra("participant_name", participantName);
        
        if (isHelper) {
            intent.putExtra(Constants.INTENT_OTHER_HELPER_ID, otherParticipantId);
        } else {
            intent.putExtra(Constants.INTENT_OTHER_USER_ID, otherParticipantId);
        }
        
        context.startActivity(intent);
        Logger.d(TAG, "Starting booking chat for booking: " + bookingId);
    }

    /**
     * Open conversations list
     */
    public static void openConversations(Context context) {
        Intent intent = new Intent(context, ConversationsActivity.class);
        context.startActivity(intent);
        Logger.d(TAG, "Opening conversations list");
    }

    /**
     * Get the appropriate participant ID based on current user type
     */
    public static Integer getOtherParticipantId(Context context, Integer senderUserId, Integer senderHelperId, 
                                               Integer receiverUserId, Integer receiverHelperId) {
        SharedPrefsHelper prefsHelper = SharedPrefsHelper.getInstance(context);
        String currentUserType = prefsHelper.getUserType();
        String currentUserId = prefsHelper.getUserId();
        
        if (currentUserId == null || currentUserType == null) {
            return null;
        }
        
        try {
            int currentId = Integer.parseInt(currentUserId);
            
            if (Constants.USER_TYPE_CUSTOMER.equals(currentUserType)) {
                // Current user is a customer, so the other participant is a helper
                return receiverHelperId != null ? receiverHelperId : senderHelperId;
            } else if (Constants.USER_TYPE_HELPER.equals(currentUserType)) {
                // Current user is a helper, so the other participant is a user
                return receiverUserId != null ? receiverUserId : senderUserId;
            }
        } catch (NumberFormatException e) {
            Logger.e(TAG, "Failed to parse current user ID: " + currentUserId, e);
        }
        
        return null;
    }

    /**
     * Check if current user can start a chat (has valid authentication)
     */
    public static boolean canStartChat(Context context) {
        SharedPrefsHelper prefsHelper = SharedPrefsHelper.getInstance(context);
        return prefsHelper.isLoggedIn() && 
               prefsHelper.getAuthToken() != null && 
               !prefsHelper.getAuthToken().isEmpty();
    }

    /**
     * Validate chat parameters before starting a chat
     */
    public static boolean validateChatParams(Integer otherUserId, Integer otherHelperId) {
        // Either otherUserId OR otherHelperId must be provided (not both, not neither)
        return (otherUserId != null && otherHelperId == null) || 
               (otherUserId == null && otherHelperId != null);
    }
}
