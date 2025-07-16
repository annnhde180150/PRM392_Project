package com.example.homehelperfinder.data.model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Model for helper work area information
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ListBookingModel {
    private int imageResId;
    private String serviceName;
    private String bookingId;
    private String price;
    private String address;
    private String dateTime;
    private String customerName;
}
