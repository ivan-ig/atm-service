package com.github.ivanig.bankserver.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    @Test
    public void cardAccountCheck() {
        Account account = new Account("22", null, "RUB", new BigDecimal("0.00"));

        assertFalse(account.isCardAccount());
    }
}