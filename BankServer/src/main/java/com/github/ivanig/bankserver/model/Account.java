package com.github.ivanig.bankserver.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Value;

import java.math.BigDecimal;

@Value
public class Account {

    String accountNumber;
    Long cardNumber;
    String currency;
    BigDecimal amount;

    @JsonIgnore
    public boolean isCardAccount() {
        return cardNumber != null;
    }
}
