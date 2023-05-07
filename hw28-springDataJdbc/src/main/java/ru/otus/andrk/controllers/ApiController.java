package ru.otus.andrk.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.otus.andrk.domain.model.Client;
import ru.otus.andrk.services.ClientsService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("${api.rest-api-prefix}/${api.rest-api-version}")
//@CrossOrigin(origins = "*")
public class ApiController {
    private static final Logger log = LoggerFactory.getLogger(ApiController.class);

    private final ClientsService clientService;

    public ApiController(ClientsService clientService) {
        this.clientService = clientService;
        log.debug("ClientsService class in ApiController is {}", clientService.getClass().getSimpleName());
    }

    @GetMapping(value = {"/clients","/clients/"})
    @Operation(summary = "Получение списка клиентов")
    public List<Client> getClients() {
        log.debug("getClients");
        return clientService.findAll();
    }

    @GetMapping(value={"/clients/{id}","/clients/{id}/"})
    @Operation(summary = "Получение клиента по Id")
    //TODO: стоит прописать возвращаемые результаты по статусам (200, ....)
    public Optional<Client> getClientById(
            @Parameter(description="Id клиента")
            @PathVariable("id") long id) {
        log.debug("getClient {}",id);
        return clientService.getClient(id);
    }

    @PostMapping(value = {"/clients","/clients/"})
    @Operation(summary = "Добавление клиента")
    public Client postClient(
            @Parameter(description = "Клиент")
            @RequestBody Client client){
        log.debug("postClient {}",client);
        return clientService.saveClient(client);
    }

}
