package com.github.ivanig.bankserver.service;

import com.github.ivanig.bankserver.entities.Account;
import com.github.ivanig.bankserver.entities.Client;
import com.github.ivanig.bankserver.exceptions.CardNotFoundException;
import com.github.ivanig.bankserver.exceptions.ClientNotFoundException;
import com.github.ivanig.bankserver.repository.ClientRepository;
import com.github.ivanig.common.messages.PinCodeStatus;
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
public class BankServerService {

    private ClientRepository clientRepository;

    public ResponseToAtm getCardAccountsInfoAndConvertToResponse(
            RequestFromAtm request) throws ClientNotFoundException {

        Set<Client> clients =
                clientRepository.findClientsByFirstNameAndLastName(request.getFirstName(), request.getLastName());

        if (clients.isEmpty()) {
            throw new ClientNotFoundException("Unable to find client [" + request.getFirstName() + " " +
                    request.getLastName() + "]; request id [" + request.getId() + "]");
        }

        Client client = findClientByCardNumber(clients, request.getCardNumber(), request.getId());
        Set<Account> cardAccounts = getClientCardAccounts(client, request.getCardNumber(), request.getPinCode());

        PinCodeStatus pinStatus = registerPinCodeStatus(cardAccounts);

        return convertClientAndAccountsToResponse(request.getId(), client, cardAccounts, pinStatus);
    }

    private Client findClientByCardNumber(
            Set<Client> clients, long cardNumber, String id) throws ClientNotFoundException {
        return clients.stream()
                .filter(client -> isClientHasCard(client, cardNumber))
                .findFirst()
                .orElseThrow(() -> new CardNotFoundException(
                        "Unable to find client with card [" + cardNumber + "]; request id [" + id + "]"));
    }

    private boolean isClientHasCard(Client client, Long cardNumber) {
        return client.getAccounts().stream()
                .anyMatch(account -> cardNumber.equals(account.getCardNumber()));
    }

    private Set<Account> getClientCardAccounts(Client client, Long cardNumber, Integer pinCode) {
        return client.getAccounts().stream()
                .filter(account -> cardNumber.equals(account.getCardNumber()))
                .filter(account -> pinCode.equals(account.getPinCode()))
                .collect(Collectors.toSet());
    }

    private PinCodeStatus registerPinCodeStatus(Set<Account> cardAccounts) {
        if (cardAccounts.isEmpty()) {
            return PinCodeStatus.INVALID;
        } else {
            return PinCodeStatus.OK;
        }
    }

    private ResponseToAtm convertClientAndAccountsToResponse(String id,
                                                             Client client,
                                                             Set<Account> cardAccounts,
                                                             PinCodeStatus pinStatus) {
        Map<String, String> accountsAndBalances =
                cardAccounts.stream().collect(Collectors.toMap(
                        Account::getNumber,
                        account -> account.getAmount() + " " + account.getCurrency()));

        Map<String, String> unmodifiableAccountsAndBalances = Collections.unmodifiableMap(accountsAndBalances);

        ResponseToAtm response = new ResponseToAtm(
                id, client.getFirstName(), client.getPatronymic(), unmodifiableAccountsAndBalances, pinStatus);

        log.debug("Converted to: [" + response + "]");
        return response;
    }
}