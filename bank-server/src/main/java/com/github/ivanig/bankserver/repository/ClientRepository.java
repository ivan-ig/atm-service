package com.github.ivanig.bankserver.repository;

import com.github.ivanig.bankserver.entities.Client;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface ClientRepository extends CrudRepository<Client, Long> {

    Set<Client> findClientsByFirstNameAndLastName(String firstName, String lastName);

}
