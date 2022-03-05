package com.github.ivanig.common.messages;

import lombok.Value;

import java.util.Map;

@Value
public class ResponseToAtm {

    Map<String, String> cardAccounts;

}