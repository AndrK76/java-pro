package ru.otus.andrk.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.otus.andrk.domain.model.Client;
import ru.otus.andrk.services.ClientsService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("${api.rest-api-prefix}/${api.rest-api-version}")
public class ApiController {
    private static final Logger log = LoggerFactory.getLogger(ApiController.class);

    private final ClientsService clientService;

    public ApiController(ClientsService clientService) {
        this.clientService = clientService;
    }

    @GetMapping(value = {"/clients","/clients/"})
    public List<Client> getClients() {
        return clientService.findAll();
    }

    @GetMapping(value={"/clients/{id}","/clients/{id}/"})
    public Optional<Client> getClientById(@PathVariable("id") long id) {
        return clientService.getClient(id);
    }

    public Client postClient(@RequestBody Client client){
        return clientService.saveClient(client);
    }

}
