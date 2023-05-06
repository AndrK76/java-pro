package ru.otus.andrk.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import ru.otus.andrk.domain.model.Address;
import ru.otus.andrk.domain.model.Client;
import ru.otus.andrk.domain.model.Phone;

import java.util.*;
import java.util.stream.Collectors;

@Service
@ConditionalOnProperty(prefix = "services", name = "clientService", havingValue = "inMemory")
public class InMemoryClientServiceImpl implements ClientsService {

    private static final Logger log = LoggerFactory.getLogger(ClientsService.class);
    private final Map<Long, Client> clients;
    private long lastClientId = 0;
    private long lastAddressId = 0;
    private long lastPhoneId = 0;

    public InMemoryClientServiceImpl() {
        log.debug("create {} service",InMemoryClientServiceImpl.class.getSimpleName());
        clients = new HashMap<>();
        var phone = new Phone(++lastPhoneId, "123-456");
        var address = new Address(++lastAddressId, "1-st Street");
        var client = new Client(++lastClientId, "Client 1", address, Set.of(phone));
        clients.put(client.getId(), client);

        phone = new Phone(++lastPhoneId, "23-456-78");
        address = new Address(++lastAddressId, "2-nd Street");
        client = new Client(++lastClientId, "Client два", address, Set.of(phone));
        clients.put(client.getId(), client);

        address = new Address(++lastAddressId, "3-я улица");
        client = new Client(++lastClientId, "Client ТРИ", address, null);
        clients.put(client.getId(), client);

        phone = new Phone(++lastPhoneId, "345-67-89");
        client = new Client(++lastClientId, "Client Four", null, Set.of(phone));
        clients.put(client.getId(), client);
    }


    @Override
    public Client saveClient(Client client) {
        if (client.getId() == null) {
            client.setId(++lastClientId);
        }
        if (client.getAddress() != null && client.getAddress().getId() == null) {
            client.getAddress().setId(++lastAddressId);
        }
        if (client.getPhones() != null) {
            for (var phone : client.getPhones()) {
                if (phone.getId() == null) {
                    phone.setId(++lastPhoneId);
                }
            }
        }
        clients.put(client.getId(), client);
        return client;
    }

    @Override
    public Optional<Client> getClient(long id) {
        return Optional.ofNullable(clients.get(id));
    }

    @Override
    public List<Client> findAll() {
        return clients.entrySet().stream().map(r -> r.getValue()).collect(Collectors.toList());
    }
}
