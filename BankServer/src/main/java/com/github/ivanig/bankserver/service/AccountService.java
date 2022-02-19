package com.github.ivanig.bankserver.service;

import com.github.ivanig.bankserver.domain.Account;
import com.github.ivanig.bankserver.dto.RequestFromAtm;
import com.github.ivanig.bankserver.dto.ResponseToAtm;

import java.util.Set;

public interface AccountService {

    ResponseToAtm getResponse(RequestFromAtm requestFromAtm);

    Set<Account> getCardAccounts(RequestFromAtm requestFromAtm);

}
