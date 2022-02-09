package com.github.ivanig.bankserver.controllers;

import com.github.ivanig.bankserver.domain.Account;
import com.github.ivanig.bankserver.repository.AccountRepository;
import com.github.ivanig.bankserver.utils.JSONUtils;
import lombok.Data;
import lombok.NonNull;

import java.util.Set;

public @Data class ServerController {
    @NonNull
    private AccountRepository accountRepository;

    public String getCardAccounts(String firstName, String lastName, long cardNumber) {

        Set<Account> cardAccounts = accountRepository
                .getAllCardAccountsByClientNameAndCardNumber(firstName, lastName, cardNumber);

//        String response = JSONUtils.toJSON(new ResponseDTO(cardAccounts));
        String response = JSONUtils.setToJSON(cardAccounts);

        return response; // JSONString для отправки на АТМ. В Spring можно обойтись без явной сериализации
    }
}
