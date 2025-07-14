package com.example.homehelperfinder.data.remote.chat;

import com.example.homehelperfinder.data.model.request.MarkAsReadRequest;
import com.example.homehelperfinder.data.model.request.SendMessageRequest;
import com.example.homehelperfinder.data.model.response.ApiResponse;
import com.example.homehelperfinder.data.model.response.ChatConversationResponse;
import com.example.homehelperfinder.data.model.response.ChatMessageResponse;
import com.example.homehelperfinder.data.model.response.MarkAsReadResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Retrofit interface for Chat API endpoints
 */
public interface ChatApiInterface {
    
    /**
     * Send a new chat message
     */
    @POST("chat/send")
    Call<ApiResponse<ChatMessageResponse>> sendMessage(@Body SendMessageRequest request);
    
    /**
     * Get conversation messages
     */
    @GET("chat/conversation")
    Call<ApiResponse<List<ChatMessageResponse>>> getConversationMessages(
            @Query("bookingId") Integer bookingId,
            @Query("otherUserId") Integer otherUserId,
            @Query("otherHelperId") Integer otherHelperId
    );
    
    /**
     * Get all conversations for the current user
     */
    @GET("chat/conversations")
    Call<ApiResponse<List<ChatConversationResponse>>> getConversations();
    
    /**
     * Get unread messages
     */
    @GET("chat/unread")
    Call<ApiResponse<List<ChatMessageResponse>>> getUnreadMessages();
    
    /**
     * Get unread messages count
     */
    @GET("chat/unread/count")
    Call<ApiResponse<Integer>> getUnreadCount();
    
    /**
     * Mark messages as read
     */
    @POST("chat/mark-as-read")
    Call<ApiResponse<MarkAsReadResponse>> markMessagesAsRead(@Body MarkAsReadRequest request);
    
    /**
     * Get chat messages for a specific booking
     */
    @GET("chat/booking/{bookingId}")
    Call<ApiResponse<List<ChatMessageResponse>>> getBookingChat(@Path("bookingId") int bookingId);
}
