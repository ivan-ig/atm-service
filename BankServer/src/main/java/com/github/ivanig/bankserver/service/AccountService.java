package com.github.ivanig.bankserver.service;

import com.github.ivanig.bankserver.model.Account;
import com.github.ivanig.bankserver.model.BankClient;
import com.github.ivanig.bankserver.repository.BankRepository;
import com.github.ivanig.common.messages.RequestFromAtm;
import com.github.ivanig.common.messages.ResponseToAtm;
import lombok.Data;
import lombok.NonNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
public class AccountService {

    @NonNull
    private BankRepository bankRepository;

    public ResponseToAtm prepareResponse(RequestFromAtm request) {
        Map<String, String> response = new HashMap<>();
        Set<Account> accounts = getCardAccounts(request);
        for (Account acc : accounts) {
            String balance = acc.getAmount() + " " + acc.getCurrency();
            response.put(acc.getAccountNumber(), balance);
        }
        return new ResponseToAtm(response);
    }

    public Set<Account> getCardAccounts(RequestFromAtm request) {

        BankClient client = bankRepository.getClientFromRepository(request).
                orElse(new BankClient("N/A", "N/A", new HashSet<>()));

        return client.getCardAccountsOnly();
    }
}

        //TODO
        // Exception (ClientNotFound ex.) вместо пустого клиента
        // Сделать Entity классы для отображения (маппинга) таблиц;
