package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

public class ProcessorSwitchFields implements Processor {
    @Override
    public Message process(Message message) {
        String field11old = message.getField11();
        String field12old = message.getField12();
        Message ret = message.toBuilder().field11(field12old).field12(field11old).build();
        return ret;
    }
}
