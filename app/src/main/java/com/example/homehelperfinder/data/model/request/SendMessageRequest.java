package com.example.homehelperfinder.data.model.request;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request model for sending a new chat message
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SendMessageRequest {
    @SerializedName("bookingId")
    private Integer bookingId;
    
    @SerializedName("receiverUserId")
    private Integer receiverUserId;
    
    @SerializedName("receiverHelperId")
    private Integer receiverHelperId;
    
    @SerializedName("messageContent")
    private String messageContent;
}
