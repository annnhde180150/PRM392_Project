package com.example.homehelperfinder.data.model.request;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request model for marking messages as read
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MarkAsReadRequest {
    @SerializedName("chatIds")
    private List<Long> chatIds;
}
