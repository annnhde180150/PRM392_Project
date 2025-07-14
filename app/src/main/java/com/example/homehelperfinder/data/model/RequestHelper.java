package com.example.homehelperfinder.data.model;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Model for helper information in admin requests
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestHelper {
    @SerializedName("id")
    private int id;
    
    @SerializedName("name")
    private String name;
}
