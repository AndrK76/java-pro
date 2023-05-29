package ru.otus.andrk.server;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.andrk.protobuf.generated.NumberGeneratorServiceGrpc;
import ru.otus.andrk.protobuf.generated.RangeMessage;
import ru.otus.andrk.protobuf.generated.ValueMessage;

import java.util.concurrent.ExecutorService;

import static ru.otus.andrk.utils.ThreadHandler.sleep;

public class NumberGeneratorServiceImpl extends NumberGeneratorServiceGrpc.NumberGeneratorServiceImplBase {
    static private final Logger log = LoggerFactory.getLogger(NumberGeneratorServiceImpl.class);
    private final ExecutorService executor;
    private final int MESSAGES_INTERVAL;

    public NumberGeneratorServiceImpl(ExecutorService executor, int messages_interval) {
        this.executor = executor;
        MESSAGES_INTERVAL = messages_interval;
        log.debug("service created");
    }

    @Override
    public void generateRange(RangeMessage request, StreamObserver<ValueMessage> responseObserver) {
        executor.submit(() -> generatorHandler(request, responseObserver));
    }

    private void generatorHandler(RangeMessage request, StreamObserver<ValueMessage> responseObserver) {
        log.info("generateRange called {}-{}", request.getFirstValue(), request.getLastValue());
        int currVal = request.getFirstValue();
        while (currVal < request.getLastValue()) {
            sleep(MESSAGES_INTERVAL);
            currVal++;
            responseObserver.onNext(ValueMessage.newBuilder().setCurrentValue(currVal).build());
            log.info("send {}", currVal);
        }
        responseObserver.onCompleted();
    }

}
