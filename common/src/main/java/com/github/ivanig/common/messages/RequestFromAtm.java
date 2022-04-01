package com.github.ivanig.common.messages;

import lombok.ToString;
import lombok.Value;

@Value
public class RequestFromAtm {

    String firstName;
    String lastName;
    long cardNumber;

    @ToString.Exclude
    int pinCode;

}