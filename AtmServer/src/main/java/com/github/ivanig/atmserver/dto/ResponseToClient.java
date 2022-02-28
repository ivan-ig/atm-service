package com.github.ivanig.atmserver.dto;

import lombok.Value;

import java.util.Map;

@Value
public class ResponseToClient {

    Map<String, String> accountView;

}