package com.github.ivanig.bankserver.service;

import com.github.ivanig.bankserver.entities.Account;
import com.github.ivanig.bankserver.entities.Client;
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
public class BankService {

    private ClientRepository clientRepository;

    public ResponseToAtm getCardAccountsInfoAndConvertToResponse(RequestFromAtm request) {
        Set<Client> clients =
                clientRepository.findClientsByFirstNameAndLastName(request.getFirstName(), request.getLastName());

        Client client = findClientByCardNumber(clients, request.getCardNumber());
        Set<Account> cardAccounts = getClientCardAccounts(client, request.getCardNumber(), request.getPinCode());

        PinCodeStatus status = registerPinCodeStatus(cardAccounts, request.getCardNumber());

        return convertClientAndAccountsToResponse(client, cardAccounts, status);
    }

    private Client findClientByCardNumber(Set<Client> clients, long cardNumber) {
        return clients.stream()
                .filter(client -> isClientHasCard(client, cardNumber))
                .findFirst()
                .orElseThrow(() -> {
                    log.error("ClientNotFoundException: Unable to find such a Client.");
                    return new ClientNotFoundException();
                });
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

    private PinCodeStatus registerPinCodeStatus(Set<Account> accounts, long cardNumber) {
        if (accounts.isEmpty()) {
            log.info("Счетчик попыток ввода пин-кода для карты " + cardNumber +
                    " в таблице БД уменьшен на единицу.");
            return PinCodeStatus.INVALID;
        } else {
            log.info("Корректный пин-код. Счетчик попыток ввода пин-кода для карты " + cardNumber +
                    " сброшен в значение по умолчанию.");
            return PinCodeStatus.OK;
        }
    }

    private ResponseToAtm convertClientAndAccountsToResponse(Client client,
                                                             Set<Account> cardAccounts,
                                                             PinCodeStatus status) {
        Map<String, String> accountsAndBalances =
                cardAccounts.stream().collect(Collectors.toMap(
                        Account::getNumber,
                        account -> account.getAmount() + " " + account.getCurrency()));

        Map<String, String> unmodifiableAccountsAndBalances = Collections.unmodifiableMap(accountsAndBalances);

        ResponseToAtm response = new ResponseToAtm(
                client.getFirstName(), client.getPatronymic(), unmodifiableAccountsAndBalances, status);

        log.info("BankServer: " + response);
        return response;
    }
}