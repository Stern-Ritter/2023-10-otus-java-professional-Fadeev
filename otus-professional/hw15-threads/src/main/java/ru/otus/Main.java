package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private final static Logger log = LoggerFactory.getLogger(Main.class);
    private final static int threadsCount = 2;
    private final static int startIntervalMillis = 1000;
    private final static int workTimeMillis = 10000;

    public static void main(String[] args) {
        ExecutorService es = Executors.newFixedThreadPool(threadsCount);
        Object monitor = new Object();

        try {
            log.info("Запуск потоков: ");
            for (int number = 1; number <= threadsCount; number++) {
                NumberPrinter numberPrinter = new NumberPrinter(number, monitor);
                log.info("Поток #{} запущен", number);
                es.execute(numberPrinter);
                Utils.sleep(startIntervalMillis);
            }

            Utils.sleep(workTimeMillis);
        } catch (InterruptedException e) {
            log.info("Основной поток прерван.");
        }
        log.info("Выключение потоков: ");
        es.shutdownNow();
    }
}