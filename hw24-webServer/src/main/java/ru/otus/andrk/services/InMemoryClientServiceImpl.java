package ru.otus.andrk.services;

import ru.otus.andrk.model.Address;
import ru.otus.andrk.model.Client;
import ru.otus.andrk.model.Phone;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class InMemoryClientServiceImpl implements ClientsService{

    private final Map<Long,Client> clients;
    private long maxId;

    public InMemoryClientServiceImpl()
    {

        clients = new HashMap<>();
        var phone = new Phone(1L, "123-456");
        var address = new Address(1L, "1-st Street");
        var client = new Client(1L,"Client 1",address, List.of(phone));
        clients.put(client.getId(), client);

        phone = new Phone(2L, "23-456-78");
        address = new Address(2L, "2-nd Street");
        client = new Client(2L,"Client два",address, List.of(phone));
        clients.put(client.getId(), client);

        address = new Address(3L, "3-я улица");
        client = new Client(3L,"Client ТРИ",address, null);
        clients.put(client.getId(), client);

        phone = new Phone(3L, "345-67-89");
        client = new Client(4L,"Client Four",null, List.of(phone));
        clients.put(client.getId(), client);

        maxId = client.getId();
    }


    @Override
    public Client saveClient(Client client) {
        if (client.getId() == null){
            client.setId(++maxId);
        }
        clients.remove(client.getId());
        clients.put(client.getId(), client);
        return client;
    }

    @Override
    public Optional<Client> getClient(long id) {
        return Optional.ofNullable(clients.get(id));
    }

    @Override
    public List<Client> findAll() {
        return clients.entrySet().stream().map(r->r.getValue()).collect(Collectors.toList());
    }
}
