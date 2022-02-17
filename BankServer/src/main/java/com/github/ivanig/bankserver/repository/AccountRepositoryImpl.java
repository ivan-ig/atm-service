package com.github.ivanig.bankserver.repository;

import com.github.ivanig.bankserver.domain.BankClient;
import lombok.Data;
import lombok.NonNull;

import java.util.HashSet;
import java.util.Set;

@Data
public class AccountRepositoryImpl implements AccountRepository {

    // Имитация базы данных
    private @NonNull Set<BankClient> dataBase;

    @Override
    public BankClient getClientFromDataBase(String firstName,
                                            String lastName,
                                            long cardNumber) {
        // Имитация работы с базой данных
        BankClient client = dataBase.stream()
                .filter(e -> firstName.equals(e.getFirstName())
                        && lastName.equals(e.getLastName()))
                .filter(e -> e.isClientHasCard(cardNumber))
                .findFirst().orElse(new BankClient(-1, "N/A", "N/A", new HashSet<>()));

        return client;
    }
}