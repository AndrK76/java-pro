package ru.otus.andrk.service;

import ru.otus.andrk.cachehw.HwCache;
import ru.otus.crm.model.Client;
import ru.otus.crm.service.DBServiceClient;

import java.util.List;
import java.util.Optional;

public class DBServiceClientWithCacheImpl implements DBServiceClient {

    final DBServiceClient dbServiceClient;
    final HwCache<String, Client> clientHwCache;

    public DBServiceClientWithCacheImpl(DBServiceClient dbServiceClient, HwCache<String, Client> clientHwCache) {
        this.dbServiceClient = dbServiceClient;
        this.clientHwCache = clientHwCache;
    }

    @Override
    public Client saveClient(Client client) {
        return dbServiceClient.saveClient(client);
    }

    @Override
    public Optional<Client> getClient(long id) {
        var cachedClient = clientHwCache.get(String.valueOf(id));
        if (cachedClient == null) {
            var client = dbServiceClient.getClient(id);
            if (client.isPresent()) {
                clientHwCache.put(String.valueOf(id), client.get());
            }
            return client;
        }
        return Optional.of(cachedClient);
    }

    @Override
    public List<Client> findAll() {
        return dbServiceClient.findAll();
    }
}
