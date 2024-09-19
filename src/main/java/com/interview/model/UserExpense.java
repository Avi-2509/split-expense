package com.interview.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserExpense {
    private String owesUserId;
    private String helperUserId;
    private Double amount;
}
