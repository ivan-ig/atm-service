package com.github.ivanig.bankserver.repository;

import com.github.ivanig.bankserver.model.BankClient;

public interface AccountRepository {

    BankClient getClientFromRepository(String firstName, String lastName, long cardNumber, int PIN);

}
