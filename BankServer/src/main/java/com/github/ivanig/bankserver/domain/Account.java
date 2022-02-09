package com.github.ivanig.bankserver.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Value;

import java.math.BigDecimal;

public @Value class Account {

    // возможны проблемы с сериализацией из-за lombok и проблемы с вытягиванием данных из БД из-за final полей

    String number;     // не уникальный для разных банков, 20 символов
    long cardNumber;        // уникальный, 16 символов
    String currency;
    BigDecimal balance;

    @JsonIgnore
    public boolean isCardAccount() {
        long cardNumberIsAbsent = -1L;
        return cardNumber != cardNumberIsAbsent;
    }

}
