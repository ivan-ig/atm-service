package com.github.ivanig.bankserver.service;

import com.github.ivanig.bankserver.entities.Account;
import com.github.ivanig.bankserver.entities.Client;
import com.github.ivanig.bankserver.exceptions.ClientNotFoundException;
import com.github.ivanig.bankserver.repository.ClientRepository;
import com.github.ivanig.common.messages.PinCodeStatus;
import com.github.ivanig.common.messages.RequestFromAtm;
import com.github.ivanig.common.messages.ResponseToAtm;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.*;

import static org.mockito.Mockito.*;

@SpringBootTest
class BankServiceTest {

    @Mock
    private ClientRepository clientRepository;

    private BankService bankService;
    private static Set<Account> accounts;
    private static Set<Client> clients;


    @BeforeAll
    public static void setUp() {
        accounts = new HashSet<Account>() {{
            Account account = new Account();
            account.setId(1L);
            account.setNumber("20");
            account.setCardNumber(16L);
            account.setPinCode(1234);
            account.setAmount(new BigDecimal("0.00"));
            account.setCurrency("RUB");
            add(account);
        }};

        clients = new HashSet<Client>() {{
            Client client = new Client();
            client.setId(1L);
            client.setFirstName("fn");
            client.setPatronymic("pa");
            client.setLastName("ln");
            client.setAccounts(accounts);
            add(client);
        }};
    }

    @BeforeEach
    public void init() {
        bankService = new BankService(clientRepository);
    }

    @Test
    public void successGetAccountsAndConvertToResponse() {
        RequestFromAtm request = new RequestFromAtm("fn", "ln", 16L, 1234);

        when(clientRepository.findClientsByFirstNameAndLastName(request.getFirstName(), request.getLastName()))
                .thenReturn(clients);

        Map<String,String> accountsAndBalances = new HashMap<String,String>() {{
            put("20", "0.00 RUB");
        }};
        ResponseToAtm expectedResponse = new ResponseToAtm(
                "fn", "pa", accountsAndBalances, PinCodeStatus.OK);

        Assertions.assertEquals(expectedResponse, bankService.getCardAccountsInfoAndConvertToResponse(request));

        verify(clientRepository, times(1))
                .findClientsByFirstNameAndLastName(request.getFirstName(), request.getLastName());
    }

    @Test
    public void successGetAccountsAndConvertToResponse_invalidPinCode() {
        RequestFromAtm request = new RequestFromAtm("fn", "ln", 16L, 9999);

        when(clientRepository.findClientsByFirstNameAndLastName(request.getFirstName(), request.getLastName()))
                .thenReturn(clients);

        ResponseToAtm expectedResponse = new ResponseToAtm(
                "fn", "pa", new HashMap<>(), PinCodeStatus.INVALID);

        Assertions.assertEquals(expectedResponse, bankService.getCardAccountsInfoAndConvertToResponse(request));

        verify(clientRepository, times(1))
                .findClientsByFirstNameAndLastName(request.getFirstName(), request.getLastName());
    }

    @Test
    public void successGetAccountsAndConvertToResponse_clientNotFoundException() {
        RequestFromAtm request = new RequestFromAtm("wrong", "wrong", 16L, 1234);

        when(clientRepository.findClientsByFirstNameAndLastName(request.getFirstName(), request.getLastName()))
                .thenReturn(Collections.emptySet());

        Assertions.assertThrows(ClientNotFoundException.class,
                () -> bankService.getCardAccountsInfoAndConvertToResponse(request));

        verify(clientRepository, times(1))
                .findClientsByFirstNameAndLastName(request.getFirstName(), request.getLastName());
    }
}