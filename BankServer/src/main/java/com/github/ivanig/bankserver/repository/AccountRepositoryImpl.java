package com.github.ivanig.bankserver.repository;

import com.github.ivanig.bankserver.domain.Account;
import com.github.ivanig.bankserver.domain.BankClient;
import lombok.Data;
import lombok.NonNull;

import java.util.HashSet;
import java.util.Set;

public @Data class AccountRepositoryImpl implements AccountRepository {

    // Имитация базы данных
    private @NonNull Set<BankClient> dataBase;

    @Override
    public Set<Account> getAllCardAccountsByClientNameAndCardNumber(String firstName,
                                                                    String lastName,
                                                                    long cardNumber) {
        // Имитация работы с базой данных
        BankClient client = dataBase.stream()
                .filter(e -> firstName.equals(e.getFirstName()) && lastName.equals(e.getLastName()))
                .filter(e -> e.isClientHasCard(cardNumber))
                .findFirst().orElse(new BankClient(-1, "N/A", "N/A", new HashSet<>()));

        // таблица БД "Клиенты":
        //          id | имя | фамилия | FK таблицы "Счета"
        // таблица "Счета":
        //                               PK | № счета | № карты | баланс | валюта

        // логику метода getCardAccountsOnly можно было бы описать SQL запросом, чтобы данные обрабатывались как можно
        // ближе к тому месту, где они хранятся, НО, к.м.к., целесообразнее возвращать из базы клиента, а не просто
        // какие-то абстрактные аккаунты; плюс, работа сервера банка, в таком случае, может быть расширена на выполнение
        // каких-то других операций, связанных не только с карточными аккаунтами.

        return client.getCardAccountsOnly();
    }
}
