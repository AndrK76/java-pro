package ru.otus.andrk;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.andrk.server.ClientSimpleWebServerImpl;
import ru.otus.andrk.server.SimpleWebServer;
import ru.otus.andrk.services.TemplateProcessor;
import ru.otus.andrk.services.TemplateProcessorImpl;

import java.io.IOException;
import java.net.InetSocketAddress;

public class WebApp {
    private static final Logger log = LoggerFactory.getLogger(WebApp.class);
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";

    public static void main(String[] args) throws Exception {
        new WebApp().runServer();
    }

    public void runServer() throws Exception {
        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        log.debug("init TemplateProcessor");
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
        log.debug("creating Server");
        SimpleWebServer webServer = new ClientSimpleWebServerImpl(
                new InetSocketAddress(WEB_SERVER_PORT),
                gson,templateProcessor);
        log.debug("starting Server");
        webServer.start();
        log.info("Server started");
        webServer.join();
        log.info("Server joined");
    }


}
