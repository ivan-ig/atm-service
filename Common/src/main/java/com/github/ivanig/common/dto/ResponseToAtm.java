package com.github.ivanig.common.dto;

import lombok.Value;

import java.util.Map;

@Value
public class ResponseToAtm {

    Map<String, String> cardAccounts;

}