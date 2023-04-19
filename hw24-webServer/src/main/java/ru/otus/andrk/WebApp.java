package ru.otus.andrk;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.andrk.model.Address;
import ru.otus.andrk.model.Client;
import ru.otus.andrk.model.Phone;
import ru.otus.andrk.server.ClientSimpleWebServerImpl;
import ru.otus.andrk.server.SimpleWebServer;
import ru.otus.andrk.services.*;
import ru.otus.andrk.hibernate.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.andrk.hibernate.repository.DataTemplateHibernate;
import ru.otus.andrk.hibernate.repository.HibernateUtils;
import ru.otus.andrk.hibernate.sessionmanager.TransactionManagerHibernate;
import ru.otus.andrk.services.TemplateProcessor;
import ru.otus.andrk.services.UserAuthService;

import java.net.InetSocketAddress;

public class WebApp {
    private static final Logger log = LoggerFactory.getLogger(WebApp.class);
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";
    private static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) throws Exception {
        new WebApp().runServer();
    }

    public void runServer() throws Exception {
        log.info("Application starting");
        Gson gson = new GsonBuilder()
                .serializeNulls()
                .excludeFieldsWithoutExposeAnnotation()
                .setPrettyPrinting()
                .create();
        log.debug("init TemplateProcessor");
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
        log.debug("init AuthService");
        UserAuthService authService = new InMemoryUserAuthServiceImpl();


        log.debug("init ClientService");
        //var clientsService = createInMemoryClientService();
        var clientsService = createDbHibernateClientService();

        log.debug("creating Server");
        SimpleWebServer webServer = new ClientSimpleWebServerImpl(
                new InetSocketAddress(WEB_SERVER_PORT),
                gson, templateProcessor, authService, clientsService);
        log.debug("starting Server");
        webServer.start();
        log.info("Server started");
        webServer.join();
        log.info("Server joined");
    }

    private ClientsService createInMemoryClientService() {
        return new InMemoryClientServiceImpl();
    }

    private ClientsService createDbHibernateClientService() {
        log.debug("create hibernate configuration");
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);
        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");
        log.debug("apply migrations");
        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();
        log.debug("configure hibernate");
        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class, Address.class, Phone.class);
        var transactionManager = new TransactionManagerHibernate(sessionFactory);
        var clientTemplate = new DataTemplateHibernate<>(Client.class);
        var dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate);
        return dbServiceClient;
    }

}
