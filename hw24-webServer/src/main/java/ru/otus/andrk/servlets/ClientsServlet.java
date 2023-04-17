package ru.otus.andrk.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.andrk.model.Address;
import ru.otus.andrk.model.Client;
import ru.otus.andrk.model.Phone;
import ru.otus.andrk.server.ServerSettings;
import ru.otus.andrk.services.ClientsService;
import ru.otus.services.TemplateProcessor;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientsServlet extends HttpServlet {
    private static final String CLIENTS_PAGE_TEMPLATE = "clients.ftl";
    private static final String TEMPLATE_CLIENTS_PAGE_ADDRESS = "page_address";

    private static final String TEMPLATE_ATTR_LIST_CLIENTS = "clients";

    private static final String TEMPLATE_ATTR_ERROR_MESSAGE = "err_msg";
    private static final String TEMPLATE_ATTR_CLIENT_NAME = "name";
    private static final String TEMPLATE_ATTR_CLIENT_ADDRESS = "address";
    private static final String TEMPLATE_ATTR_CLIENT_PHONE = "phone";


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
        paramsMap.put(TEMPLATE_CLIENTS_PAGE_ADDRESS, ServerSettings.clientsPage);
        paramsMap.put(TEMPLATE_ATTR_LIST_CLIENTS, allClients);
        paramsMap.put(TEMPLATE_ATTR_CLIENT_ADDRESS,"");
        paramsMap.put(TEMPLATE_ATTR_CLIENT_PHONE,"");
        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(CLIENTS_PAGE_TEMPLATE, paramsMap));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        var newClientInfo = request.getParameterMap();
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put(TEMPLATE_CLIENTS_PAGE_ADDRESS, ServerSettings.clientsPage);

        String clientName = newClientInfo.get(TEMPLATE_ATTR_CLIENT_NAME)[0];
        String clientAddress = newClientInfo.get(TEMPLATE_ATTR_CLIENT_ADDRESS)[0];
        String clientPhone = newClientInfo.get(TEMPLATE_ATTR_CLIENT_PHONE)[0];
        if (!clientName.isEmpty()) {
            Address address = clientAddress.isEmpty() ? null : new Address(clientAddress);
            List<Phone> phones = clientPhone.isEmpty() ? null : List.of(new Phone(clientPhone));
            Client client = new Client(clientName, address, phones);
            clientsService.saveClient(client);
            paramsMap.put(TEMPLATE_ATTR_CLIENT_ADDRESS,"");
            paramsMap.put(TEMPLATE_ATTR_CLIENT_PHONE,"");
        } else {
            paramsMap.put(TEMPLATE_ATTR_ERROR_MESSAGE,"Имя клиента обязательно для ввода");
            paramsMap.put(TEMPLATE_ATTR_CLIENT_ADDRESS,clientAddress);
            paramsMap.put(TEMPLATE_ATTR_CLIENT_PHONE,clientPhone);
        }


        var allClients = clientsService.findAll();
        paramsMap.put(TEMPLATE_ATTR_LIST_CLIENTS, allClients);
        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(CLIENTS_PAGE_TEMPLATE, paramsMap));
    }
}
