package com.example.homehelperfinder.data.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddMoneytoIncomeRequest {
    private int helperId;
    private double amount;
}
