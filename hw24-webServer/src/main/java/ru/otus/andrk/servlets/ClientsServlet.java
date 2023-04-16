package ru.otus.andrk.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.andrk.services.ClientsService;
import ru.otus.services.TemplateProcessor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ClientsServlet extends HttpServlet {
    private static final String CLIENTS_PAGE_TEMPLATE = "clients.ftl";
    private static final String TEMPLATE_ATTR_LIST_CLIENTS = "clients";

    private final ClientsService clientsService;

    private final TemplateProcessor templateProcessor;

    public ClientsServlet(ClientsService clientsService, TemplateProcessor templateProcessor) {
        this.clientsService = clientsService;
        this.templateProcessor = templateProcessor;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> paramsMap = new HashMap<>();
        var allClients = clientsService.findAll();
        paramsMap.put(TEMPLATE_ATTR_LIST_CLIENTS,allClients);
        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(CLIENTS_PAGE_TEMPLATE, paramsMap));
    }
}
