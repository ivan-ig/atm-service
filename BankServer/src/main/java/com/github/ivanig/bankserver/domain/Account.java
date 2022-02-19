package com.github.ivanig.bankserver.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Value;

import java.math.BigDecimal;

@Value
public class Account {

    String accountNumber;
    long cardNumber;
    String currency;
    BigDecimal amount;

    @JsonIgnore
    public boolean isCardAccount() {
        long cardNumberIsAbsent = -1L;
        return cardNumber != cardNumberIsAbsent;
    }
}
