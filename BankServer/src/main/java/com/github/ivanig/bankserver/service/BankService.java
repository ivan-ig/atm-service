package com.github.ivanig.bankserver.service;

import com.github.ivanig.bankserver.controller.BankServerController;
import com.github.ivanig.bankserver.entities.Account;
import com.github.ivanig.bankserver.entities.Client;
import com.github.ivanig.bankserver.exceptions.CardNotFoundException;
import com.github.ivanig.bankserver.exceptions.ClientNotFoundException;
import com.github.ivanig.bankserver.exceptions.InvalidPinCodeException;
import com.github.ivanig.bankserver.repository.ClientRepository;
import com.github.ivanig.common.messages.RequestFromAtm;
import com.github.ivanig.common.messages.ResponseToAtm;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class BankService implements BankServerController {

    private ClientRepository clientRepository;

    @Override
    public ResponseToAtm getCardAccountsInfoAndConvertToResponse(RequestFromAtm request) {

        log.info("BankServer: " + request);

        Set<Client> clients =
                clientRepository.findClientsByFirstNameAndLastName(request.getFirstName(), request.getLastName());

        Client client = findClientByCardAndPin(clients, request.getCardNumber(), request.getPinCode());
        Set<Account> cardAccounts = getClientCardAccounts(client, request.getCardNumber(), request.getPinCode());

        return convertClientAndAccountsToResponse(client, cardAccounts);
    }

    private Client findClientByCardAndPin(Set<Client> clients, Long cardNumber, Integer pinCode) {
        return clients.stream()
                .filter(client -> isClientHasCardAccount(client, cardNumber, pinCode))
                .findFirst()
                .orElseThrow(() -> new ClientNotFoundException("Unable to find client."));
    }

    private boolean isClientHasCardAccount(Client client, Long cardNumber, Integer pinCode) {
        return client.getAccounts().stream()
                .anyMatch(account -> isAccountContainsCardAndPin(account, cardNumber, pinCode));
    }

    private boolean isAccountContainsCardAndPin(Account account, Long cardNumber, Integer pinCode) {
        if (!cardNumber.equals(account.getCardNumber())) {
            log.info("request.cardNumber: " + cardNumber + ", account.getCardNumber: " + account.getCardNumber());
            throw new CardNotFoundException("The card is invalid, contact the bank.");
        }
        if (!pinCode.equals(account.getPinCode())) {
            log.info("request.pinCode: " + pinCode + ", account.getPinCode()" + account.getPinCode());
            throw new InvalidPinCodeException("Access denied. Wrong PIN-code entered.");
        }
        return true;
    }

    private Set<Account> getClientCardAccounts(Client client, Long cardNumber, Integer pinCode) {
        return client.getAccounts().stream()
                .filter(account -> cardNumber.equals(account.getCardNumber()))
                .filter(account -> pinCode.equals(account.getPinCode()))
                .collect(Collectors.toSet());
    }

    private ResponseToAtm convertClientAndAccountsToResponse(Client client, Set<Account> cardAccounts) {

        Map<String, String> accountsAndBalances = cardAccounts.stream().collect(Collectors.toMap(
                Account::getNumber,
                account -> account.getAmount() + " " + account.getCurrency()));

        Map<String, String> unmodifiableAccountsAndBalances = Collections.unmodifiableMap(accountsAndBalances);

        ResponseToAtm response =
                new ResponseToAtm(client.getFirstName(), client.getPatronymic(), unmodifiableAccountsAndBalances);
        log.info("BankServer: " + response);

        return response;
    }
}