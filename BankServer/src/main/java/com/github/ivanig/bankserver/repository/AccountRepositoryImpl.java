package com.github.ivanig.bankserver.repository;

import com.github.ivanig.bankserver.domain.BankClient;
import com.github.ivanig.bankserver.repository.dao.H2DatabaseAccessor;
import lombok.Data;
import lombok.NonNull;

import java.util.HashSet;

@Data
public class AccountRepositoryImpl implements AccountRepository {

    @NonNull
    private H2DatabaseAccessor h2DatabaseAccessor;

    @Override
    public BankClient getClientFromRepository(String firstName, String lastName, long cardNumber) {

        return h2DatabaseAccessor.getClientFromH2DB(firstName, lastName, cardNumber)
                .orElse(new BankClient(-1L, "N/A", "N/A", new HashSet<>()));
    }
}