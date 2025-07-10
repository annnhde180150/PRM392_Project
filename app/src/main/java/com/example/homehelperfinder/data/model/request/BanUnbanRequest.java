package com.example.homehelperfinder.data.model.request;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BanUnbanRequest {
    @SerializedName("profileId")
    private int profileId;

    @SerializedName("profileType")
    private String profileType;

    @SerializedName("reason")
    private String reason;
}
