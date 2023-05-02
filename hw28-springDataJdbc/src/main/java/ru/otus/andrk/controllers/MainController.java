package ru.otus.andrk.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class MainController {

    private static final Logger log = LoggerFactory.getLogger(MainController.class);


    @GetMapping("/")
    public String index(Model model) {
        log.debug("get index");
        return "index";
    }

    @GetMapping("/clients")
    public String clients(Model model) {
        log.debug("get clients");
        return "clients";
    }

    @GetMapping("/clients_ajax")
    public String clients_ajax(Model model) {
        log.debug("get clients_ajax");
        return "clients_ajax";
    }
}
