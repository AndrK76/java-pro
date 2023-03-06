package ru.otus.listener.homework;

import ru.otus.listener.Listener;
import ru.otus.model.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HistoryListener implements Listener, HistoryReader {

    Map<Long,Message> messages = new HashMap<>();

    @Override
    public void onUpdated(Message msg) {
        Message.Builder builder = msg.toBuilder();
        if (msg.getField13() != null){
            builder.field13(msg.getField13().getClone());
        }
        messages.put(msg.getId(),builder.build());
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        var histMsg = messages.get(id);
        if (histMsg==null){
            return Optional.empty();
        }
        return Optional.of(histMsg);
    }
}
