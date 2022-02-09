package com.github.ivanig.bankserver.repository;

import com.github.ivanig.bankserver.domain.Account;

import java.util.Set;

public interface AccountRepository {
    Set<Account> getAllCardAccountsByClientNameAndCardNumber(String firstName,
                                                             String lastName,
                                                             long cardNumber);
}
