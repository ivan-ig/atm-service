package com.github.ivanig.bankserver.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
public class Account {

    @Id
    private Long id;

    @Column(value = "ACCOUNT_NUMBER")
    private String number;

    @Column(value = "CARD_NUMBER")
    private Long cardNumber;

    @Column(value = "PIN_CODE")
    private Integer pinCode;

    @Column(value = "AMOUNT")
    private BigDecimal amount;

    @Column(value = "CURRENCY")
    private String currency;


}
