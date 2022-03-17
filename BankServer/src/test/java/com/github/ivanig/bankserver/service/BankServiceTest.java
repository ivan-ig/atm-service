package com.github.ivanig.bankserver.service;

class BankServiceTest {

//    private static BankRepository bankRepository;
//    private static BankService bankService;
//    private static RequestFromAtm request;
//
//    @BeforeAll
//    public static void setUp() {
//
//        bankRepository = Mockito.mock(BankRepository.class);
//        bankService = new BankService(bankRepository);
//
//        request = new RequestFromAtm("1", "2", 16L, 1234);
//    }
//
//    @Test
//    public void successPrepareResponse() {
//
//        Set<Account> accounts = new HashSet<Account>(){{
//            add(new Account("20", null, "RUB", new BigDecimal("0.00")));
//            add(new Account("21", 16L, "RUB", new BigDecimal("0.00")));
//            add(new Account("22", 16L, "USD", new BigDecimal("0.00")));
//        }};
//
//        when(bankRepository.getClient(request)).then(e ->
//                Optional.of(new BankClient("any", "any", accounts)));
//
//        Map<String, String> map = new HashMap<String, String>() {{
//            put("21", "0.00 RUB");
//            put("22", "0.00 USD");
//        }};
//        ResponseToAtm expectedResponse = new ResponseToAtm(map);
//
//        Assertions.assertEquals(expectedResponse, bankService.getAccountsInfoConvertedToResponse(request));
//    }
//
//    @Test
//    public void failPrepareResponse() {
//
//        when(bankRepository.getClient(request)).then(e -> Optional.ofNullable(null));
//
//        Assertions.assertThrows(ClientNotFoundException.class, () -> bankService.getAccountsInfoConvertedToResponse(request));
//    }
}