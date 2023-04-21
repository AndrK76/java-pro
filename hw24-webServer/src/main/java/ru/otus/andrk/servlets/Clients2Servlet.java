package ru.otus.andrk.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.andrk.server.ServerPages;
import ru.otus.andrk.services.TemplateProcessor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Clients2Servlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(Clients2Servlet.class);
    private static final String CLIENTS_PAGE_TEMPLATE = "clients2.ftl";
    private static final String TEMPLATE_CLIENTS_PAGE_ADDRESS = "page_address";
    private static final String TEMPLATE_CLIENTS2_PAGE_ADDRESS = "page2_address";
    private static final String TEMPLATE_CLIENTS_API_ADDRESS = "api_address";


    private final TemplateProcessor templateProcessor;

    public Clients2Servlet(TemplateProcessor templateProcessor) {
        this.templateProcessor = templateProcessor;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("get {}",((Request)request).getOriginalURI());
        var paramsMap = createParamMap();
        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(CLIENTS_PAGE_TEMPLATE, paramsMap));
    }

    private Map<String, Object> createParamMap() {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put(TEMPLATE_CLIENTS_PAGE_ADDRESS, ServerPages.clientsPage);
        paramsMap.put(TEMPLATE_CLIENTS2_PAGE_ADDRESS, ServerPages.clientsWithApiPage);
        paramsMap.put(TEMPLATE_CLIENTS_API_ADDRESS, ServerPages.clientApiRoot);
        return paramsMap;
    }


}
