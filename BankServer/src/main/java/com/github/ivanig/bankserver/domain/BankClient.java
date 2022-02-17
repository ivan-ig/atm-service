package com.github.ivanig.bankserver.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Value;

import java.util.Set;
import java.util.stream.Collectors;

@Value
public class BankClient {

    long id;
    String firstName;
    String lastName;

    @Getter(AccessLevel.NONE)
    Set<Account> accounts;

    public Set<Account> getCardAccountsOnly() {
        return accounts.stream().
                filter(Account::isCardAccount)
                .collect(Collectors.toSet());
    }

    public boolean isClientHasCard(long cardNumber) {
        return accounts.stream().anyMatch(e -> e.getCardNumber() == cardNumber);
    }
}
