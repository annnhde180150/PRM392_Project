package com.example.homehelperfinder.data.model.request;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HelperRequest {
    @SerializedName("phoneNumber")
    public String phoneNumber;

    @SerializedName("email")
    public String email;

    @SerializedName("password")
    public String password;

    @SerializedName("fullName")
    public String fullName;

    @SerializedName("dateOfBirth")
    public String dateOfBirth;

    @SerializedName("bio")
    public String bio;

    @SerializedName("gender")
    public String gender;

    public List<HelperSkillRequest> skills;
    public List<HelperWorkAreaRequest> workAreas;
    public List<HelperDocumentRequest> documents;

    public void setDateOfBirth(String rawDate) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date date = inputFormat.parse(rawDate);

            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            this.dateOfBirth = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            this.dateOfBirth = rawDate;
        }
    }


    private String getStringParsedDateOfBirth() {
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        try {
            Date date = inputFormat.parse(dateOfBirth);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return dateOfBirth; // fallback to raw string
        }
    }


}
