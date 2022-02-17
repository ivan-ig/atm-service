package com.github.ivanig.bankserver.repository;

import com.github.ivanig.bankserver.domain.BankClient;

public interface AccountRepository {

    BankClient getClientFromDataBase(String firstName,
                                     String lastName,
                                     long cardNumber);
}
