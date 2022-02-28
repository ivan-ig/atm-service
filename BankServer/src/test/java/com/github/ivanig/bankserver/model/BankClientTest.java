package com.github.ivanig.bankserver.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

class BankClientTest {

    @Test
    public void failedGettingCardAccountsOnly() {

        Account nonCardAccount = new Account("number", null, "CUR", BigDecimal.valueOf(0.00));
        Account cardAccount = new Account("number", 1616161616161616L, "CUR", BigDecimal.valueOf(0.00));

        Set<Account> allAccounts = new HashSet<>();
        allAccounts.add(nonCardAccount);
        allAccounts.add(cardAccount);

        BankClient client = new BankClient("name", "surname", allAccounts);

        assertNotEquals(2, client.getCardAccountsOnly().size());
    }
}