package com.github.ivanig.bankserver.dto;

import lombok.Value;

@Value
public class RequestFromAtm {

    String firstName;
    String lastName;
    long cardNumber;

}