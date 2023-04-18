package ru.otus.andrk.servlets;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.andrk.model.Client;
import ru.otus.andrk.services.ClientsService;

import java.io.IOException;
import java.util.Optional;


public class ClientApiServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(ClientsServlet.class);
    private static final int ID_PATH_PARAM_POSITION = 3;
    private final ClientsService clientsService;
    private final Gson gson;

    public ClientApiServlet(ClientsService clientsService, Gson gson) {
        this.clientsService = clientsService;
        this.gson = gson;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("get {}", ((Request) request).getOriginalURI());
        Long id = extractIdFromRequest(request);
        var out = getOut(response);
        if (id == null) {
            var users = clientsService.findAll();
            out.print(gson.toJson(users));
        } else {
            var client = clientsService.getClient(id);
            writeClientToOut(out, client);
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("post {}", ((Request) request).getOriginalURI());
        var out = getOut(response);
        Client client = gson.fromJson(request.getReader(),Client.class);
        client = clientsService.saveClient(client);
        writeClientToOut(out, Optional.of(client));
    }

    private Long extractIdFromRequest(HttpServletRequest request) {
        String[] path = request.getRequestURI().split("/");
        return (path.length > ID_PATH_PARAM_POSITION) ? Long.valueOf(path[ID_PATH_PARAM_POSITION]) : null;
    }

    private ServletOutputStream getOut(HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.addHeader("Origin", "any");
        response.addHeader("Access-Control-Allow-Origin", "*");
        ServletOutputStream out = response.getOutputStream();
        return out;
    }

    private void writeClientToOut(ServletOutputStream out, Optional<Client> user) throws IOException {
        if (user.isPresent()){
            out.print(gson.toJson(user.get()));
        } else{
            out.print(gson.toJson(user));
        }
    }


}
