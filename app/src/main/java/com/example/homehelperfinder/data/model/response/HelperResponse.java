package com.example.homehelperfinder.data.model.response;

import com.example.homehelperfinder.data.model.request.HelperDocumentRequest;
import com.example.homehelperfinder.data.model.request.HelperSkillRequest;
import com.example.homehelperfinder.data.model.request.HelperWorkAreaRequest;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HelperResponse {

    @SerializedName("id")
    public int id;

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

    @SerializedName("dateOfBirth")
    public String dateOfBirth;

    @SerializedName("bio")
    public String bio;

    @SerializedName("gender")
    public String gender;

    @SerializedName("isActive")
    public Boolean isActive;

    public List<HelperSkillResponse> skills;
    public List<HelperWorkAreaResponse> workAreas;
    public List<HelperDocumentResponse> documents;
}
