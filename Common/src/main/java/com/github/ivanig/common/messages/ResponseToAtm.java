package com.github.ivanig.common.messages;

import lombok.*;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class ResponseToAtm {

    private String firstname;
    private String patronymic;
    private Map<String, String> accountsAndBalances;
    private PinCodeStatus pinCodeStatus;

}