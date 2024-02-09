package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.handler.ComplexProcessor;
import ru.otus.handler.Handler;
import ru.otus.listener.homework.HistoryListener;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;
import ru.otus.processor.Processor;
import ru.otus.processor.homework.BaseCurrentTimeProvider;
import ru.otus.processor.homework.ProcessorSwapField11Field12;
import ru.otus.processor.homework.ProcessorThrowsExceptionEvenSecond;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class HomeWork {

    /*
    Реализовать to do:
      1. Добавить поля field11 - field13 (для field13 используйте класс ObjectForMessage)
      2. Сделать процессор, который поменяет местами значения field11 и field12
      3. Сделать процессор, который будет выбрасывать исключение в четную секунду (сделайте тест с гарантированным результатом)
            Секунда должна определяьться во время выполнения.
            Тест - важная часть задания
            Обязательно посмотрите пример к паттерну Мементо!
      4. Сделать Listener для ведения истории (подумайте, как сделать, чтобы сообщения не портились)
         Уже есть заготовка - класс HistoryListener, надо сделать его реализацию
         Для него уже есть тест, убедитесь, что тест проходит
    */

    private static final Logger logger = LoggerFactory.getLogger(HomeWork.class);

    public static void main(String[] args) {
        /*
          по аналогии с Demo.class
          из элеменов "to do" создать new ComplexProcessor и обработать сообщение
        */

        Processor processorSwapField11Field12 = new ProcessorSwapField11Field12();
        Processor processorThrowsExceptionEvenSecond = new ProcessorThrowsExceptionEvenSecond(new BaseCurrentTimeProvider());
        Consumer<Exception> errorHandler = (e) -> logger.info("Error: {}", e.getMessage());
        List<Processor> processors = List.of(processorSwapField11Field12, processorThrowsExceptionEvenSecond);
        Handler complexProcessor = new ComplexProcessor(processors, errorHandler);

        HistoryListener historyListener = new HistoryListener();
        complexProcessor.addListener(historyListener);

        List<String> field13Data = new ArrayList<>();
        field13Data.add("Field 13 value");
        ObjectForMessage field13 = new ObjectForMessage();
        field13.setData(field13Data);

        long id = 1L;
        Message message = new Message.Builder(id)
                .field1("field1")
                .field2("field2")
                .field3("field3")
                .field6("field6")
                .field10("field10")
                .field11("field11")
                .field12("field12")
                .field13(field13)
                .build();

        field13.setData(Collections.emptyList());
        field13Data.clear();

        Message resultMsg = complexProcessor.handle(message);
        logger.info("Result message: {}", resultMsg);

        Message historyMsg = historyListener.findMessageById(id).get();
        logger.info("History message: {}", historyMsg);

        complexProcessor.removeListener(historyListener);
    }
}
