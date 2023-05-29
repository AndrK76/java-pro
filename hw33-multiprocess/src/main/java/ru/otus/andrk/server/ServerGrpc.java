package ru.otus.andrk.server;

import io.grpc.ServerBuilder;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static ru.otus.andrk.utils.ConfigHandler.getConfig;

public class ServerGrpc implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(ServerGrpc.class);

    private final int SERVER_PORT;
    private final int MESSAGES_INTERVAL;

    private final ExecutorService executor;

    public ServerGrpc() {
        var config = getConfig("server.properties");
        SERVER_PORT = Integer.parseInt(config.getProperty("server.port"));
        MESSAGES_INTERVAL = Integer.parseInt(config.getProperty("messages.interval"));
        executor = Executors.newFixedThreadPool(Integer.parseInt(config.getProperty("server.threads")));
    }

    @SneakyThrows
    @Override
    public void run() {
        log.info("Starting server");
        var numberGeneratorService = new NumberGeneratorServiceImpl(executor, MESSAGES_INTERVAL);
        log.debug("numberGeneratorService created");
        var server = ServerBuilder
                .forPort(SERVER_PORT)
                .addService(numberGeneratorService)
                .build();
        log.debug("Server configured");
        server.start();
        log.info("Server started");
        log.info("Awaiting connections on port {}", SERVER_PORT);
        server.awaitTermination();
        executor.shutdownNow();
        executor.awaitTermination(0, TimeUnit.MILLISECONDS);
    }
}
