package ru.otus.listener.homework;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import ru.otus.listener.Listener;
import ru.otus.model.Message;

public class HistoryListener implements Listener, HistoryReader {
    private final Map<Long, Message> messages;

    public HistoryListener() {
        this.messages = new HashMap<>();
    }

    @Override
    public void onUpdated(Message msg) {
        Long id = msg.getId();
        Message msgCopy = msg.copy();
        messages.put(id, msgCopy);
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        return Optional.ofNullable(messages.get(id));
    }
}
