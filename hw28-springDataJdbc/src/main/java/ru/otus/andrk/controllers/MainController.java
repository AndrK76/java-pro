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

    @GetMapping("/fragments/{fragment}")
    public String fragments(@PathVariable String fragment, Model model) {
        log.debug("get fragments {}",fragment);
        return "fragments/" + fragment;
    }
}
