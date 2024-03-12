package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.printer.NumberPrinter;
import ru.otus.sequence.IntegerSequence;
import ru.otus.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class Main {
    private final static Logger log = LoggerFactory.getLogger(Main.class);
    private final static int numberOfThreads = 2;
    private final static int startIntervalMillis = 500;
    private final static int printIntervalMillis = 500;
    private final static int workTimeMillis = 50000;

    public static void main(String[] args) {
        List<Thread> workingThreads = new ArrayList<>();
        Object monitor = new Object();

        try {
            log.info("Запуск потоков: ");
            for (int number = 1; number <= numberOfThreads; number++) {
                Thread thread = new Thread(new NumberPrinter(numberOfThreads, printIntervalMillis, monitor, number,
                        new IntegerSequence()));

                log.info("Поток #{} запущен", number);
                workingThreads.add(thread);
                thread.start();
                Utils.sleep(startIntervalMillis);
            }

            Utils.sleep(workTimeMillis);
        } catch (InterruptedException e) {
            log.info("Основной поток прерван.");
        }

        log.info("Выключение потоков: ");
        for (Thread thread : workingThreads) {
            thread.interrupt();
        }
    }
}