package com.example.homehelperfinder.data.remote.chat;

import android.content.Context;

import com.example.homehelperfinder.data.model.request.MarkAsReadRequest;
import com.example.homehelperfinder.data.model.request.SendMessageRequest;
import com.example.homehelperfinder.data.model.response.ChatConversationResponse;
import com.example.homehelperfinder.data.model.response.ChatMessageResponse;
import com.example.homehelperfinder.data.model.response.MarkAsReadResponse;
import com.example.homehelperfinder.data.remote.BaseApiService;
import com.example.homehelperfinder.data.remote.RetrofitClient;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Service class for Chat API operations
 */
public class ChatApiService extends BaseApiService {
    private ChatApiInterface apiInterface;
    private Context context;

    public ChatApiService(Context context) {
        super();
        this.context = context.getApplicationContext();
        RetrofitClient.init(this.context);
        this.apiInterface = RetrofitClient.getChatApiService();
    }

    // Send Message
    public CompletableFuture<ChatMessageResponse> sendMessage(Context context, SendMessageRequest request) {
        return executeCall(context, apiInterface.sendMessage(request), "sendMessage");
    }

    public void sendMessage(Context context, SendMessageRequest request, ApiCallback<ChatMessageResponse> callback) {
        handleApiResponse(context, sendMessage(context, request), callback);
    }

    // Get Conversation Messages
    public CompletableFuture<List<ChatMessageResponse>> getConversationMessages(Context context, Integer bookingId, Integer otherUserId, Integer otherHelperId) {
        return executeCall(context, apiInterface.getConversationMessages(bookingId, otherUserId, otherHelperId), "getConversationMessages");
    }

    public void getConversationMessages(Context context, Integer bookingId, Integer otherUserId, Integer otherHelperId, ApiCallback<List<ChatMessageResponse>> callback) {
        handleApiResponse(context, getConversationMessages(context, bookingId, otherUserId, otherHelperId), callback);
    }

    // Get All Conversations
    public CompletableFuture<List<ChatConversationResponse>> getConversations(Context context) {
        return executeCall(context, apiInterface.getConversations(), "getConversations");
    }

    public void getConversations(Context context, ApiCallback<List<ChatConversationResponse>> callback) {
        handleApiResponse(context, getConversations(context), callback);
    }

    // Get Unread Messages
    public CompletableFuture<List<ChatMessageResponse>> getUnreadMessages(Context context) {
        return executeCall(context, apiInterface.getUnreadMessages(), "getUnreadMessages");
    }

    public void getUnreadMessages(Context context, ApiCallback<List<ChatMessageResponse>> callback) {
        handleApiResponse(context, getUnreadMessages(context), callback);
    }

    // Get Unread Count
    public CompletableFuture<Integer> getUnreadCount(Context context) {
        return executeCall(context, apiInterface.getUnreadCount(), "getUnreadCount");
    }

    public void getUnreadCount(Context context, ApiCallback<Integer> callback) {
        handleApiResponse(context, getUnreadCount(context), callback);
    }

    // Mark Messages as Read
    public CompletableFuture<MarkAsReadResponse> markMessagesAsRead(Context context, MarkAsReadRequest request) {
        return executeCall(context, apiInterface.markMessagesAsRead(request), "markMessagesAsRead");
    }

    public void markMessagesAsRead(Context context, MarkAsReadRequest request, ApiCallback<MarkAsReadResponse> callback) {
        handleApiResponse(context, markMessagesAsRead(context, request), callback);
    }

    // Get Booking Chat
    public CompletableFuture<List<ChatMessageResponse>> getBookingChat(Context context, int bookingId) {
        return executeCall(context, apiInterface.getBookingChat(bookingId), "getBookingChat");
    }

    public void getBookingChat(Context context, int bookingId, ApiCallback<List<ChatMessageResponse>> callback) {
        handleApiResponse(context, getBookingChat(context, bookingId), callback);
    }
}
