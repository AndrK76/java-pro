package ru.otus.andrk.example;

import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.andrk.protobuf.generated.Empty;
import ru.otus.andrk.protobuf.generated.NumberGeneratorServiceGrpc;
import ru.otus.andrk.protobuf.generated.UserMessage;

import java.net.ConnectException;
import java.util.concurrent.CountDownLatch;

import static ru.otus.andrk.example.Mapper.userMessage2User;
import static ru.otus.andrk.utils.ConfigHandler.getConfig;

public class SampleClient implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(SampleClient.class);

    private final String SERVER_HOST;
    private final int SERVER_PORT;

    public SampleClient() {
        var config = getConfig("client.properties");
        SERVER_HOST = config.getProperty("server.host");
        SERVER_PORT = Integer.parseInt(config.getProperty("server.port"));
    }

    @Override
    public void run() {
        log.info("Start");
        try {
            doWork();
        } catch (RuntimeException e) {
            processError(e);
        }
    }

    private void doWork() {
        var channel = ManagedChannelBuilder
                .forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();
        var blockingStub = NumberGeneratorServiceGrpc.newBlockingStub(channel);

        var user = new User(0, "Вася", "Пупкин");
        //log.debug("User: {}", user);

        var saveUserMsg = blockingStub.saveUser(
                UserMessage.newBuilder().setFirstName(user.getFirstName()).setLastName(user.getLastName()).build()
        );
        user.setId(saveUserMsg.getId());
        log.info("User saved: {}", user);

        log.info("Users List:");
        var allUsersIterator = blockingStub.findAllUsers(Empty.getDefaultInstance());
        allUsersIterator.forEachRemaining(s -> log.info("{}", userMessage2User(s)));

        var latch = new CountDownLatch(1);
        var noBlockingStub = NumberGeneratorServiceGrpc.newStub(channel);
        log.info("Start get all");
        noBlockingStub.findAllUsers(Empty.getDefaultInstance(), new StreamObserver<UserMessage>() {
            @Override
            public void onNext(UserMessage value) {
                log.info("{}", userMessage2User(value));
            }

            @Override
            public void onError(Throwable t) {
                log.error("{}", t);
            }

            @Override
            public void onCompleted() {
                log.info("end get all");
                latch.countDown();
            }
        });
        log.info("Просто текст");
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        channel.shutdown();

    }

    private void processError(RuntimeException e) {
        if (e.getCause() != null
                & ConnectException.class.isAssignableFrom(e.getCause().getClass())) {
            log.error("Don't Connect: {}", e.getCause().getMessage());
        } else {
            throw e;
        }
    }


}
