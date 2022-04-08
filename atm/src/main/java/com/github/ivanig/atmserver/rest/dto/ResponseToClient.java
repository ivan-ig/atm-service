package com.github.ivanig.atmserver.rest.dto;

import lombok.Value;

import java.util.Map;

@Value
public class ResponseToClient {

    String id;
    String clientName;
    Map<String, String> accountsView;
    String pinCodeStatus;

}