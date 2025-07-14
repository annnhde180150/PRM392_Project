package com.example.homehelperfinder.data.model.request;

import com.example.homehelperfinder.data.model.response.HelperSkillResponse;
import com.example.homehelperfinder.data.model.response.HelperWorkAreaResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HelperUpdateRequest {
    private String phoneNumber;
    private String email;
    private String fullName;
    private String profilePictureUrl;
    private String bio;
    private String idDocumentUrl;
    private String cvDocumentUrl;
    private List<HelperSkillResponse> skills;
    private List<HelperWorkAreaResponse> workAreas;
}
