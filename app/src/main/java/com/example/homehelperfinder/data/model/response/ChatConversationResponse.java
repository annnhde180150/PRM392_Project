package com.example.homehelperfinder.data.model.response;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Response model for chat conversations
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatConversationResponse {
    @SerializedName("conversationId")
    private String conversationId;
    
    @SerializedName("bookingId")
    private Integer bookingId;
    
    @SerializedName("participantUserId")
    private Integer participantUserId;
    
    @SerializedName("participantHelperId")
    private Integer participantHelperId;
    
    @SerializedName("participantName")
    private String participantName;
    
    @SerializedName("participantProfilePicture")
    private String participantProfilePicture;
    
    @SerializedName("participantType")
    private String participantType;
    
    @SerializedName("lastMessage")
    private ChatMessageResponse lastMessage;
    
    @SerializedName("unreadCount")
    private int unreadCount;
    
    @SerializedName("lastActivity")
    private String lastActivity;
}
