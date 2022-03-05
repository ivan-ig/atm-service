package com.github.ivanig.common.messages;

import lombok.Value;

@Value
public class RequestFromAtm {

    String firstName;
    String lastName;
    long cardNumber;
    int PIN;

}