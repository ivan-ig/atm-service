package com.github.ivanig.bankserver.repository.dao;

import com.github.ivanig.bankserver.domain.BankClient;

import java.util.Optional;

public interface H2DatabaseAccessor {

    Optional<BankClient> getClientFromH2DB(String firstName, String lastName, long cardNumber);
}
