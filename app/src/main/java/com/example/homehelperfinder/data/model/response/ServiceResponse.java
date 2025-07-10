package com.example.homehelperfinder.data.model.response;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceResponse {

    @SerializedName("serviceId")
    private int serviceId;

    @SerializedName("serviceName")
    private String serviceName;

    @SerializedName("serviceDescription")
    private String serviceDescription;

    @SerializedName("iconUrl")
    private String iconUrl;

    @SerializedName("basePrice")
    private double basePrice;

    @SerializedName("unitPrice")
    private double unitPrice;

    @SerializedName("isActive")
    private boolean isActive;

    @SerializedName("parentServiceId")
    private int parentServiceId;


}
