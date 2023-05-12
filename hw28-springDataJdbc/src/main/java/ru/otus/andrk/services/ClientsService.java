package ru.otus.andrk.services;

import ru.otus.andrk.domain.model.Client;


import java.util.List;
import java.util.Optional;

public interface ClientsService {
    Client saveClient(Client client);

    Optional<Client> getClient(long id);

    List<Client> findAll();
}
