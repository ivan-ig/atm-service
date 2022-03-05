package com.github.ivanig.bankserver.service;

import com.github.ivanig.bankserver.exceptions.ClientNotFoundException;
import com.github.ivanig.bankserver.model.Account;
import com.github.ivanig.bankserver.model.BankClient;
import com.github.ivanig.bankserver.repository.BankRepository;
import com.github.ivanig.common.messages.RequestFromAtm;
import com.github.ivanig.common.messages.ResponseToAtm;
import lombok.Data;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Data
public class BankService {

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
                orElseThrow(() -> new ClientNotFoundException("An error occurred while searching for a Client"));

        return client.getCardAccountsOnly();
    }
}

        //TODO
        // Сделать Entity классы для отображения (маппинга) таблиц;
