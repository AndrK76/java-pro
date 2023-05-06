package ru.otus.andrk.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import ru.otus.andrk.domain.model.Client;
import ru.otus.andrk.orm.repository.ClientRepository;
import ru.otus.andrk.orm.sessionmanager.TransactionManager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@ConditionalOnProperty(prefix = "services", name = "clientService", havingValue = "jdbc")
public class JdbcClientServiceImpl implements ClientsService {

    private static final Logger log = LoggerFactory.getLogger(ClientsService.class);

    private final TransactionManager transactionManager;
    private final ClientRepository clientRepository;

    public JdbcClientServiceImpl(TransactionManager transactionManager, ClientRepository clientRepository) {
        this.transactionManager = transactionManager;
        this.clientRepository = clientRepository;
        log.debug("create {} service", JdbcClientServiceImpl.class.getSimpleName());
    }


    @Override
    public Client saveClient(Client client) {
        log.debug("save client");
        return transactionManager.doInTransaction(() -> clientRepository.save(client));

    }

    @Override
    public Optional<Client> getClient(long id) {
        return clientRepository.findById(id);
    }

    @Override
    public List<Client> findAll() {
        log.debug("start find all");
        var clientList = new ArrayList<Client>();
        var ret = clientRepository.findAll();
        ret.forEach(r -> clientList.add(r));
        clientList.sort(Comparator.comparing(Client::getId));
        return clientList;
    }
}
