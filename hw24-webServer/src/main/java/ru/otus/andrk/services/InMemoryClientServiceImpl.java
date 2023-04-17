package ru.otus.andrk.services;

import ru.otus.andrk.model.Address;
import ru.otus.andrk.model.Client;
import ru.otus.andrk.model.Phone;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class InMemoryClientServiceImpl implements ClientsService {

    private final Map<Long, Client> clients;
    private long lastClientId = 0;
    private long lastAddressId = 0;
    private long lastPhoneId = 0;

    public InMemoryClientServiceImpl() {

        clients = new HashMap<>();
        var phone = new Phone(++lastPhoneId, "123-456");
        var address = new Address(++lastAddressId, "1-st Street");
        var client = new Client(++lastClientId, "Client 1", address, List.of(phone));
        clients.put(client.getId(), client);

        phone = new Phone(++lastPhoneId, "23-456-78");
        address = new Address(++lastAddressId, "2-nd Street");
        client = new Client(++lastClientId, "Client два", address, List.of(phone));
        clients.put(client.getId(), client);

        address = new Address(++lastAddressId, "3-я улица");
        client = new Client(++lastClientId, "Client ТРИ", address, null);
        clients.put(client.getId(), client);

        phone = new Phone(++lastPhoneId, "345-67-89");
        client = new Client(++lastClientId, "Client Four", null, List.of(phone));
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
