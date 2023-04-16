package ru.otus.andrk.server;

import com.google.gson.Gson;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.andrk.services.ClientsService;
import ru.otus.andrk.servlets.ClientsServlet;
import ru.otus.services.TemplateProcessor;
import ru.otus.services.UserAuthService;
import ru.otus.helpers.FileSystemHelper;
import ru.otus.servlets.AuthorizationFilter;
import ru.otus.servlets.LoginServlet;

import java.net.InetSocketAddress;
import java.util.Arrays;

public final class ClientSimpleWebServerImpl implements SimpleWebServer {

    private static final Logger log = LoggerFactory.getLogger(ClientSimpleWebServerImpl.class);
    private static final String START_PAGE_NAME = "index.html";
    private static final String COMMON_RESOURCES_DIR = "static";

    private final Gson gson;
    private final TemplateProcessor templateProcessor;

    private final UserAuthService authService;

    private final ClientsService clientsService;

    private final Server server;

    public ClientSimpleWebServerImpl(
            InetSocketAddress address,
            Gson gson,
            TemplateProcessor templateProcessor,
            UserAuthService authService, ClientsService clientsService) {
        this.gson = gson;
        this.templateProcessor = templateProcessor;
        this.server = new Server(address);
        this.authService = authService;
        this.clientsService = clientsService;
    }


    @Override
    public void start() throws Exception {
        if (server.getHandlers().length == 0) {
            initContext();
        }
        server.start();
        log.debug("server started");
    }

    @Override
    public void join() throws Exception {
        server.join();
        log.debug("server joined");
    }

    @Override
    public void stop() throws Exception {
        server.stop();
    }

    private void initContext() {
        log.debug("start initContext");
        ResourceHandler resourceHandler = createResourceHandler();
        ServletContextHandler servletContextHandler = createServletContextHandler();

        HandlerList handlers = new HandlerList();
        handlers.addHandler(resourceHandler);
        handlers.addHandler(applySecurity(servletContextHandler, "/clients", "/api/client/*"));

        server.setHandler(handlers);
        log.debug("end initContext");
    }

    private Handler applySecurity(ServletContextHandler servletContextHandler, String... paths) {
        servletContextHandler.addServlet(new ServletHolder(new LoginServlet(templateProcessor, authService)), "/login");
        AuthorizationFilter authorizationFilter = new AuthorizationFilter();
        Arrays.stream(paths).forEachOrdered(path -> servletContextHandler.addFilter(new FilterHolder(authorizationFilter), path, null));
        return servletContextHandler;
    }


    private ResourceHandler createResourceHandler() {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(false);
        resourceHandler.setWelcomeFiles(new String[]{START_PAGE_NAME});
        resourceHandler.setResourceBase(FileSystemHelper.localFileNameOrResourceNameToFullPath(COMMON_RESOURCES_DIR));
        return resourceHandler;
    }

    private ServletContextHandler createServletContextHandler() {
        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletContextHandler.addServlet(new ServletHolder(new ClientsServlet(clientsService, templateProcessor)), "/clients");
        //servletContextHandler.addServlet(new ServletHolder(new UsersApiServlet(userDao, gson)), "/api/user/*");
        return servletContextHandler;
    }

}
