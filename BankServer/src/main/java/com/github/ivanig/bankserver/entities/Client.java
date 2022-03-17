package com.github.ivanig.bankserver.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;

import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
public class Client {

    @Id
    private Long id;

    @Column(value = "FIRST_NAME")
    private String firstName;

    @Column(value = "PATRONYMIC")
    private String patronymic;

    @Column(value = "LAST_NAME")
    private String lastName;

    @MappedCollection(idColumn = "OWNER_ID")
    private Set<Account> accounts;

}