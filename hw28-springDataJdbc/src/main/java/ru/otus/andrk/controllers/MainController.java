package ru.otus.andrk.controllers;

import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.andrk.domain.model.Address;
import ru.otus.andrk.domain.model.Client;
import ru.otus.andrk.domain.model.Phone;
import ru.otus.andrk.dto.ClientNameError;
import ru.otus.andrk.services.ClientsService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
public class MainController {

    private static final Logger log = LoggerFactory.getLogger(MainController.class);
    private final ClientsService clientService;


    public MainController(ClientsService clientService) {
        this.clientService = clientService;
        log.debug("ClientsService class in MainController is {}", clientService.getClass().getSimpleName());
    }

    @GetMapping("/")
    public String index(Model model) {
        log.debug("get index");
        return "index";
    }

    @GetMapping("/clients")
    public String clients(Model model) {
        log.debug("get clients");
        model.addAttribute("clientList", clientService.findAll());
        model.addAttribute("newClient", new Client());
        model.addAttribute("clientNameError", new ClientNameError());
        return "clients";
    }

    @PostMapping("/clients")
    public String clientsPost(Model model,
                              @RequestParam(name = "name") String name,
                              @RequestParam(name = "address") String address,
                              @RequestParam(name = "phone") String phone) {
        //TODO: Можно сделать DTO и её передавать (будет проще, не стал делать, без DTO с Thymeleaf интереснее поиграться получилось
        log.debug("post client");

        Client client = new Client(
                Strings.isNotBlank(name) ? name : null,
                Strings.isNotBlank(address) ? new Address(address) : null,
                Strings.isNotBlank(phone) ?  Set.of(new Phone(phone)) : null);
        if (client.getName() != null){
            clientService.saveClient(client);
            model.addAttribute("clientNameError", new ClientNameError());
            model.addAttribute("newClient", new Client());
        } else{
            model.addAttribute("clientNameError", new ClientNameError("Имя клиента должно быть заполнено"));
            model.addAttribute("newClient", client);
        }
        model.addAttribute("clientList", clientService.findAll());

        //TODO:Redirect не стал делать, хочу ошибку передавать, можно попробовать через сессию и кэш, и тогда Redirect пойдёт
        return "clients";
    }

    @GetMapping("/clients_ajax")
    public String clients_ajax(Model model) {
        log.debug("get clients_ajax");
        return "clients_ajax";
    }
}
