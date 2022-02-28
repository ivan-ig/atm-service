package com.github.ivanig.bankserver.repository;

import com.github.ivanig.bankserver.model.BankClient;
import com.github.ivanig.bankserver.repository.dao.H2DatabaseAccessor;
import lombok.Data;
import lombok.NonNull;

import java.util.HashSet;

@Data
public class AccountRepositoryImpl implements AccountRepository {

    @NonNull
    private H2DatabaseAccessor h2DatabaseAccessor;

    @Override
    public BankClient getClientFromRepository(String firstName, String lastName, long cardNumber, int PIN) {

        return h2DatabaseAccessor.getClientFromH2DB(firstName, lastName, cardNumber, PIN)
                .orElse(new BankClient("N/A", "N/A", new HashSet<>()));
        //TODO
        // Exception (ClientNotFound ex.) вместо пустого клиента
        // Возможно будет нужны Entity классы для отображения (маппинга) таблиц
    }
}