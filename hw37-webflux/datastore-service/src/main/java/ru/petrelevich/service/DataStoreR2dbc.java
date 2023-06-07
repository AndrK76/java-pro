package ru.petrelevich.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import ru.petrelevich.domain.Message;
import ru.petrelevich.repository.MessageRepository;

import java.time.Duration;

import static java.time.temporal.ChronoUnit.MILLIS;

@Service
public class DataStoreR2dbc implements DataStore {
    private static final Logger log = LoggerFactory.getLogger(DataStoreR2dbc.class);
    private final MessageRepository messageRepository;

    private final Scheduler workerPool;

    private final int MESSAGE_DELAY;


    public DataStoreR2dbc(
            Scheduler workerPool,
            MessageRepository messageRepository,
            @Value("${messages.delay}") int message_delay) {
        this.workerPool = workerPool;
        this.messageRepository = messageRepository;
        MESSAGE_DELAY = message_delay;
    }

    @Override
    public Mono<Message> saveMessage(Message message) {
        log.info("saveMessage:{}", message);
        return messageRepository.save(message);
    }

    @Override
    public Flux<Message> loadMessages(String roomId) {
        return Mono.just(roomId)
                .publishOn(workerPool)
                .doOnNext(room -> log.info("start loadMessages roomId:{}", room))
                .flatMapMany(messageRepository::findByRoomId)
                .publishOn(workerPool)
                .doOnNext(msg -> log.info("loaded msg:{}", msg))
                .delayElements(Duration.of(MESSAGE_DELAY, MILLIS), workerPool)
                ;
    }

    @Override
    public Flux<Message> loadAllMessages() {
        return  Mono.just("")
                .publishOn(workerPool)
                .doOnNext(s -> log.info("Start loadAllMessages"))
                .publishOn(workerPool)
                .thenMany(messageRepository.findAll())
                .doOnNext(msg -> log.info("loaded msg:{}", msg))
                .delayElements(Duration.of(MESSAGE_DELAY, MILLIS), workerPool);
    }
}
