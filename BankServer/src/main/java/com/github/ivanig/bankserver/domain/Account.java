package com.github.ivanig.bankserver.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.math.BigDecimal;

@Value
public class Account {

    String accountNumber;
    long cardNumber;
    String currency;
    BigDecimal balance;

    @JsonIgnore
    public boolean isCardAccount() {
        long cardNumberIsAbsent = -1L;
        return cardNumber != cardNumberIsAbsent;
    }
}
