package ru.otus.andrk.example;

import io.grpc.ServerBuilder;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ru.otus.andrk.utils.ConfigHandler.getConfig;

public class SampleServer implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(SampleServer.class);
    private final int SERVER_PORT;

    private final DBService dbService;

    public SampleServer() {
        var config = getConfig("server.properties");
        SERVER_PORT = Integer.parseInt(config.getProperty("server.port"));
        dbService = new FakeDbServiceImpl();

    }

    @SneakyThrows
    @Override
    public void run() {
        log.info("Starting");
        var numberGeneratorService = new NumberGeneratorServiceImpl(dbService);
        var server = ServerBuilder
                .forPort(SERVER_PORT)
                .addService(numberGeneratorService)
                .build();
        server.start();
        log.info("Awaiting connections on port {}", SERVER_PORT);
        server.awaitTermination();

    }
}
