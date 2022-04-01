package com.github.ivanig.atmserver.dto;

import lombok.Value;

import java.util.Map;

@Value
public class ResponseToClient {

    String clientName;
    Map<String, String> accountsView;
    String pinCodeStatus;

}