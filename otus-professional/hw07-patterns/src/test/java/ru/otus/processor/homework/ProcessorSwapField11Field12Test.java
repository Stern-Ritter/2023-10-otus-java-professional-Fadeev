package ru.otus.processor.homework;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.handler.ComplexProcessor;
import ru.otus.handler.Handler;
import ru.otus.model.Message;
import ru.otus.processor.Processor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

class ProcessorSwapField11Field12Test {
    private final static Logger logger = LoggerFactory.getLogger(ProcessorSwapField11Field12Test.class);
    private Handler complexProcessor;
    private List<Processor> processors;
    private Consumer<Exception> errorHandler;

    @BeforeEach
    void setUp() {
        Processor processor = new ProcessorSwapField11Field12();
        processors = new ArrayList<>(List.of(processor));
        errorHandler = e -> logger.info("Error: {}", e.getMessage());
        complexProcessor = new ComplexProcessor(processors, errorHandler);
    }

    @Test
    void process() {
        String field11 = "Field 11 value";
        String field12 = "Field 12 value";
        Message message = new Message.Builder(1L).field11(field11).field12(field12).build();
        Message resultMessage = complexProcessor.handle(message);

        String actualField11Value = resultMessage.getField11();
        String actualField12Value = resultMessage.getField12();
        Assertions.assertEquals(field12, actualField11Value);
        Assertions.assertEquals(field11, actualField12Value);
    }
}