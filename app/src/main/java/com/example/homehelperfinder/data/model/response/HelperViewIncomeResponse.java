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
public class HelperViewIncomeResponse {
    @SerializedName("walletId")
    private int walletId;
    @SerializedName("helperId")
    private int helperId;
    @SerializedName("balance")
    private double balance;
    @SerializedName("currencyCode")
    private String currencyCode;
}
