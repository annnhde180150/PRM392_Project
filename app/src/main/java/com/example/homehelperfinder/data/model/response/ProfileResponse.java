package com.example.homehelperfinder.data.model.response;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileResponse {
    @SerializedName("profileId")
    private int profileId;

    @SerializedName("profileType")
    private String profileType;

    @SerializedName("fullName")
    private String fullName;

    @SerializedName("email")
    private String email;

    @SerializedName("phoneNumber")
    private String phoneNumber;

    @SerializedName("isActive")
    private Boolean isActive;

    @SerializedName("registrationDate")
    private String registrationDate;

    @SerializedName("lastLoginDate")
    private String lastLoginDate;

    private Date parseApiDate(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }

        try {
            SimpleDateFormat inputFormat;
            if (dateString.contains(".")) {
                // Format with milliseconds: 2025-06-20T16:33:01.85
                inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS", Locale.getDefault());
            } else if (dateString.contains("T")) {
                // Format without milliseconds: 2025-06-20T16:33:01
                inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            } else {
                // Simple date format: 2025-06-20
                inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            }
            return inputFormat.parse(dateString);
        } catch (ParseException e) {
            return null;
        }
    }
} 