package com.example.homehelperfinder.data.model.response;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Response model for chat messages
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageResponse {
    @SerializedName("chatId")
    private long chatId;
    
    @SerializedName("bookingId")
    private Integer bookingId;
    
    @SerializedName("senderUserId")
    private Integer senderUserId;
    
    @SerializedName("senderHelperId")
    private Integer senderHelperId;
    
    @SerializedName("receiverUserId")
    private Integer receiverUserId;
    
    @SerializedName("receiverHelperId")
    private Integer receiverHelperId;
    
    @SerializedName("messageContent")
    private String messageContent;
    
    @SerializedName("timestamp")
    private String timestamp;
    
    @SerializedName("isReadByReceiver")
    private Boolean isReadByReceiver;
    
    @SerializedName("readTimestamp")
    private String readTimestamp;
    
    @SerializedName("isModerated")
    private Boolean isModerated;
    
    @SerializedName("moderatorAdminId")
    private Integer moderatorAdminId;
    
    @SerializedName("senderName")
    private String senderName;
    
    @SerializedName("senderProfilePicture")
    private String senderProfilePicture;
    
    @SerializedName("senderType")
    private String senderType;
}
