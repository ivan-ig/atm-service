package com.github.ivanig.bankserver.service;

import com.github.ivanig.bankserver.model.Account;
import com.github.ivanig.common.dto.RequestFromAtm;
import com.github.ivanig.common.dto.ResponseToAtm;

import java.util.Set;

public interface AccountService {

    ResponseToAtm prepareResponse(RequestFromAtm requestFromAtm);

    Set<Account> getCardAccounts(RequestFromAtm requestFromAtm);

}
