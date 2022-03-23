package com.github.ivanig.bankserver.service;

import com.github.ivanig.bankserver.entities.Account;
import com.github.ivanig.bankserver.entities.Client;
import com.github.ivanig.bankserver.repository.ClientRepository;
import com.github.ivanig.common.messages.PinCodeStatus;
import com.github.ivanig.common.messages.RequestFromAtm;
import com.github.ivanig.common.messages.ResponseToAtm;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BankServiceTest {

    @Autowired
    private MockMvc mvc;

    private static ClientRepository clientRepository;
    private static BankService bankService;
    private static Set<Account> accounts;
    private static Set<Client> clients;

    @BeforeAll
    public static void setUp() {
        clientRepository = Mockito.mock(ClientRepository.class);
        bankService = new BankService(clientRepository);

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

    @Test
    public void successGetAccountsAndConvertToResponse() {
        RequestFromAtm request = new RequestFromAtm("fn", "ln", 16L, 1234);

        when(clientRepository.findClientsByFirstNameAndLastName(request.getFirstName(), request.getLastName()))
                .then(e -> clients);

        Map<String,String> accountsAndBalances = new HashMap<String,String>() {{
            put("20", "0.00 RUB");
        }};
        ResponseToAtm expectedResponse = new ResponseToAtm(
                "fn", "pa", accountsAndBalances, PinCodeStatus.OK);

        Assertions.assertEquals(expectedResponse, bankService.getCardAccountsInfoAndConvertToResponse(request));
    }

    @Test
    public void successGetAccountsAndConvertToResponse_InvalidPinCode() {
        RequestFromAtm request = new RequestFromAtm("fn", "ln", 16L, 9999);

        when(clientRepository.findClientsByFirstNameAndLastName(request.getFirstName(), request.getLastName()))
                .then(e -> clients);

        ResponseToAtm expectedResponse = new ResponseToAtm(
                "fn", "pa", new HashMap<>(), PinCodeStatus.INVALID);

        Assertions.assertEquals(expectedResponse, bankService.getCardAccountsInfoAndConvertToResponse(request));
    }

    @Test
    public void successGetResponse() throws Exception {
        mvc.perform(post("/clientInfo").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"ANDREY\"," +
                                "\"lastName\":\"VASILIEV\"," +
                                "\"cardNumber\":\"1616161616161111\"," +
                                "\"pinCode\":\"1234\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"firstname\":\"ANDREY\"," +
                        "\"patronymic\":\"IVANOVICH\"," +
                        "\"accountsAndBalances\":{\"77777777777777733333\":\"0.00 EUR\"," +
                        "\"77777777777777722222\":\"0.00 USD\"," +
                        "\"77777777777777711111\":\"0.00 RUB\"}," +
                        "\"pinCodeStatus\":\"OK\"}"));
    }

    @Test
    public void failGetResponse_4xxCodeDueToInvalidCardNumber() throws Exception {
        mvc.perform(post("/clientInfo").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"AFANASII\"," +
                                "\"lastName\":\"FET\"," +
                                "\"cardNumber\":\"9999999\"," +
                                "\"pinCode\":\"1234\"}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void failGetResponse_4xxCodeDueToInvalidClientName() throws Exception {
        mvc.perform(post("/clientInfo").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"BOBA\"," +
                                "\"lastName\":\"FETT\"," +
                                "\"cardNumber\":\"1616161616162222\"," +
                                "\"pinCode\":\"1234\"}"))
                .andExpect(status().is4xxClientError());
    }
}