package com.example.homehelperfinder.data.model.response;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private int id;
    private String phoneNumber;
    private String email;
    private String fullName;
    private String profilePictureUrl;
    private Date registrationDate;
    private Date lastLoginDate;
    private String externalAuthProvider;
    private String externalAuthId;
    private boolean isActive;
    private Integer defaultAddressId;
    private String role;
}
