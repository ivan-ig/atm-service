package com.github.ivanig.bankserver.dto;

import com.github.ivanig.bankserver.domain.Account;
import lombok.Value;

import java.util.Set;

public @Value class Response {

    Set<Account> cardAccounts;

}
