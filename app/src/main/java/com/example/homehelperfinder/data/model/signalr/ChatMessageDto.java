package com.example.homehelperfinder.data.model.signalr;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDto {

    @SerializedName("chatId")
    private Long chatId;

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

    public boolean isFromCurrentUser(Integer currentUserId, Integer currentHelperId) {
        if (currentUserId != null && senderUserId != null) {
            return currentUserId.equals(senderUserId);
        }
        if (currentHelperId != null && senderHelperId != null) {
            return currentHelperId.equals(senderHelperId);
        }
        return false;
    }

    public boolean isForCurrentUser(Integer currentUserId, Integer currentHelperId) {
        if (currentUserId != null && receiverUserId != null) {
            return currentUserId.equals(receiverUserId);
        }
        if (currentHelperId != null && receiverHelperId != null) {
            return currentHelperId.equals(receiverHelperId);
        }
        return false;
    }
}
