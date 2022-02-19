package com.github.ivanig.bankserver.service;

import com.github.ivanig.bankserver.domain.Account;
import com.github.ivanig.bankserver.domain.BankClient;
import com.github.ivanig.bankserver.dto.RequestFromAtm;
import com.github.ivanig.bankserver.dto.ResponseToAtm;
import com.github.ivanig.bankserver.repository.AccountRepository;
import lombok.Data;
import lombok.NonNull;

import java.util.Set;

@Data
public class AccountServiceImpl implements AccountService {

    @NonNull
    private AccountRepository accountRepository;

    @Override
    public ResponseToAtm getResponse(RequestFromAtm request) {
        return new ResponseToAtm(getCardAccounts(request));
    }

    @Override
    public Set<Account> getCardAccounts(RequestFromAtm request) {
        String firstName = request.getFirstName();
        String lastName = request.getLastName();
        long cardNumber = request.getCardNumber();

        BankClient client = accountRepository.getClientFromRepository(firstName, lastName, cardNumber);

        return client.getCardAccountsOnly();
    }
}