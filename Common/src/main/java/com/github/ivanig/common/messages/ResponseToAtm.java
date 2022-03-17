package com.github.ivanig.common.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class ResponseToAtm {

    private String firstname;
    private String patronymic;
    private Map<String, String> accountsAndBalances;

}