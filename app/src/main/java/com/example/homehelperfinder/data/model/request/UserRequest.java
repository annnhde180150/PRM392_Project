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
public class UserRequest {
    @SerializedName("phoneNumber")
    public String phoneNumber;

    @SerializedName("email")
    public String email;

    @SerializedName("password")
    public String password;

    @SerializedName("fullName")
    public String fullName;

    @SerializedName("profilePictureUrl")
    public String profilePictureUrl;

    @SerializedName("address")
    public String address;

    @SerializedName("defaultAddressId")
    public Integer defaultAddressId;
} 