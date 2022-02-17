package com.github.ivanig.bankserver.dto;

import com.github.ivanig.bankserver.domain.Account;
import lombok.Value;

import java.util.Set;

@Value
public class ResponseToAtm {

    Set<Account> cardAccounts;

}
