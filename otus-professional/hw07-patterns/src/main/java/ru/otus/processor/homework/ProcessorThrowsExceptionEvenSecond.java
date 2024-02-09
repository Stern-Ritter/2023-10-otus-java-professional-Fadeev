package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;
import ru.otus.processor.homework.exceptions.EvenSecondException;

public class ProcessorThrowsExceptionEvenSecond implements Processor {
    private final CurrentTimeProvider currentTimeProvider;

    public ProcessorThrowsExceptionEvenSecond(CurrentTimeProvider currentTimeProvider) {
        this.currentTimeProvider = currentTimeProvider;
    }

    @Override
    public Message process(Message message) {
        long seconds = currentTimeProvider.getCurrentTimeSeconds();
        if (seconds % 2 == 0) {
            throw new EvenSecondException(String.format("Current second is even: %d\n", seconds));
        }
        return message;
    }
}
