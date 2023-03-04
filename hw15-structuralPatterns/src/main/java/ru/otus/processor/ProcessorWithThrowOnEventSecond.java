package ru.otus.processor;

import ru.otus.model.Message;

import java.time.LocalDateTime;

public class ProcessorWithThrowOnEventSecond implements Processor {

    private final TimeProvider timeProvider;

    public ProcessorWithThrowOnEventSecond(TimeProvider timeProvider) {
        this.timeProvider = timeProvider;
    }

    public ProcessorWithThrowOnEventSecond() {
        this(LocalDateTime::now);
    }

    @Override
    public Message process(Message message) {
        var thisTime = timeProvider.now();
        if ((thisTime.getSecond() % 2) == 0) {
            throw new InvalidTimeException(thisTime);
        }
        return message.toBuilder().build();
    }
}
