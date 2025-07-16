package com.example.homehelperfinder.data.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserAddressUpdateRequest {

    public int userId;

    public String addressLine1;

    public String addressLine2;

    public String ward;

    public String district;

    public String city;

    public String fullAddress;

    public double latitude;

    public double longitude;
}
