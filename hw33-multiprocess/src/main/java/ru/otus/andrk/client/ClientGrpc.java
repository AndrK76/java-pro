package ru.otus.andrk.client;

import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.andrk.model.ServerValue;
import ru.otus.andrk.protobuf.generated.NumberGeneratorServiceGrpc;
import ru.otus.andrk.protobuf.generated.RangeMessage;

import java.net.ConnectException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static ru.otus.andrk.utils.ConfigHandler.getConfig;

public class ClientGrpc implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(ClientGrpc.class);

    private final String SERVER_HOST;
    private final int SERVER_PORT;
    private final int END_RANGE;
    private final int TIMEOUT;
    private final int ITERATIONS_COUNT;

    private final ServerValue serverValue = new ServerValue(0);
    private boolean canContinue;


    public ClientGrpc() {
        var config = getConfig("client.properties");
        SERVER_HOST = config.getProperty("server.host");
        SERVER_PORT = Integer.parseInt(config.getProperty("server.port"));
        END_RANGE = Integer.parseInt(config.getProperty("request.range.end"));
        TIMEOUT = Integer.parseInt(config.getProperty("client.timeout"));
        ITERATIONS_COUNT = Integer.parseInt(config.getProperty("client.iterations"));
    }

    @Override
    public void run() {
        try {
            doWork();
        } catch (Exception e) {
            processError(e);
        }
    }

    private void doWork() throws InterruptedException {
        ExecutorService executor = Executors.newCachedThreadPool();

        log.info("Start");

        var latch = new CountDownLatch(1);
        canContinue = true;

        executor.submit(() -> {
            processData();
            latch.countDown();
        });

        executor.submit(() -> {
            getServerValue();
        });

        latch.await();
        executor.shutdownNow();
        executor.awaitTermination(0, TimeUnit.MILLISECONDS);
        log.info("Finish");
    }

    private void processData() {
        int currentValue = 0;
        currentValue = calculateAndShowValue(0, currentValue, null);
        sleep(TIMEOUT);
        for (int i = 0; (i < ITERATIONS_COUNT) & canContinue; i++) {
            currentValue = calculateAndShowValue(i + 1, currentValue, serverValue.getValue());
            sleep(TIMEOUT);
        }
    }

    private int calculateAndShowValue(int iteration, int currentValue, ServerValue currServerValue) {
        var delta = iteration == 0 ? 0 : 1;
        StringBuilder formula = new StringBuilder();
        formula.append(currentValue);
        if (delta != 0) {
            formula.append("+");
            formula.append(delta);
        }
        var ret = currentValue + delta;
        if (currServerValue != null && !currServerValue.isApplied()) {
            formula.append("+");
            formula.append(currServerValue.value());
            ret += currServerValue.value();
            serverValue.apply(currServerValue.value());
        }

        log.info("Iteration: {}, Current value: {}, Formula: {}", iteration, ret, formula.toString());
        return ret;
    }


    private void getServerValue() {
        try {
            var channel = ManagedChannelBuilder
                    .forAddress(SERVER_HOST, SERVER_PORT)
                    .usePlaintext()
                    .build();
            var request = RangeMessage
                    .newBuilder()
                    .setFirstValue(serverValue.getValue().value())
                    .setLastValue(END_RANGE)
                    .build();

            var stub = NumberGeneratorServiceGrpc.newBlockingStub(channel);
            var valueIterator = stub.generateRange(request);
            valueIterator.forEachRemaining(r -> {
                int currVal = r.getCurrentValue();
                log.info("Server Value {}", currVal);
                serverValue.setValue(currVal);
            });
        } catch (RuntimeException e) {
            if (canContinue) {
                log.error(e.getCause().getMessage());
                canContinue = false;
            }
        }
    }


    private void sleep(int timeout) {
        try {
            Thread.currentThread().sleep(timeout);
        } catch (InterruptedException e) {
        }
    }

    private void processError(Throwable e) {
        if (e.getClass() == InterruptedException.class) {
        } else if (ConnectException.class.isAssignableFrom(e.getClass())
                || e.getCause() != null
                & ConnectException.class.isAssignableFrom(e.getCause().getClass())) {
            log.error("Don't Connect: {}", e.getCause().getMessage());
        } else {
            throw new RuntimeException(e);
        }
    }

}
