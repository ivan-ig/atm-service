package com.github.ivanig.common.messages;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class RequestFromAtm {

    private String id;
    private String firstName;
    private String lastName;
    private long cardNumber;

    @ToString.Exclude
    private int pinCode;

}