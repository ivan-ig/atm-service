package com.github.ivanig.bankserver.service;

import com.github.ivanig.bankserver.model.Account;
import com.github.ivanig.bankserver.model.BankClient;
import com.github.ivanig.bankserver.repository.AccountRepository;
import com.github.ivanig.common.dto.RequestFromAtm;
import com.github.ivanig.common.dto.ResponseToAtm;
import lombok.Data;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Data
public class AccountServiceImpl implements AccountService {

    @NonNull
    private AccountRepository accountRepository;

    @Override
    public ResponseToAtm prepareResponse(RequestFromAtm request) {
        Map<String, String> response = new HashMap<>();
        Set<Account> accounts = getCardAccounts(request);
        for (Account acc : accounts) {
            String balance = acc.getAmount() + " " + acc.getCurrency();
            response.put(acc.getAccountNumber(), balance);
        }
        return new ResponseToAtm(response);
    }

    @Override
    public Set<Account> getCardAccounts(RequestFromAtm request) {
        String firstName = request.getFirstName();
        String lastName = request.getLastName();
        long cardNumber = request.getCardNumber();
        int PIN = request.getPIN();

        BankClient client = accountRepository.getClientFromRepository(firstName, lastName, cardNumber, PIN);

        return client.getCardAccountsOnly();
    }
}