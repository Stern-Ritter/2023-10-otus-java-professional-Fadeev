package ru.otus.processor.homework;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.handler.ComplexProcessor;
import ru.otus.handler.Handler;
import ru.otus.model.Message;
import ru.otus.processor.Processor;
import ru.otus.processor.homework.exceptions.EvenSecondException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

class ProcessorThrowsExceptionEvenSecondTest {
    private final static Logger logger = LoggerFactory.getLogger(ProcessorSwapField11Field12Test.class);

    @Test
    void processEvenCurrentTimeSecond() {
        CurrentTimeProvider evenSecondDateTimeProvider = () -> 2;
        Processor processor = new ProcessorThrowsExceptionEvenSecond(evenSecondDateTimeProvider);
        List<Processor> processors = new ArrayList<>(List.of(processor));
        Consumer<Exception> errorHandler = e -> {
            throw new RuntimeException(e);
        };
        Handler complexProcessor = new ComplexProcessor(processors, errorHandler);

        Message message = new Message.Builder(1L).build();
        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> complexProcessor.handle(message));
        Assertions.assertInstanceOf(EvenSecondException.class, exception.getCause());
    }

    @Test
    void processOddCurrentTimeSecond() {
        CurrentTimeProvider evenSecondDateTimeProvider = () -> 3;
        Processor processor = new ProcessorThrowsExceptionEvenSecond(evenSecondDateTimeProvider);
        List<Processor> processors = new ArrayList<>(List.of(processor));
        Consumer<Exception> errorHandler = e -> {
            throw new RuntimeException(e);
        };
        Handler complexProcessor = new ComplexProcessor(processors, errorHandler);

        Message message = new Message.Builder(1L).build();
        Message resultMessage = complexProcessor.handle(message);
        Assertions.assertEquals(message, resultMessage);
    }
}