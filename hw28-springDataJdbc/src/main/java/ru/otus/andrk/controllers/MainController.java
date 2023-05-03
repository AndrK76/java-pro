package ru.otus.andrk.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.andrk.domain.model.Client;
import ru.otus.andrk.services.ClientsService;

import java.util.List;

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
        List<Client> clients = clientService.findAll();
        model.addAttribute("clientList", clients);
        return "clients";
    }

    @GetMapping("/clients_ajax")
    public String clients_ajax(Model model) {
        log.debug("get clients_ajax");
        return "clients_ajax";
    }
}
