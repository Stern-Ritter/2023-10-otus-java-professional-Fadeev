package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NumberPrinter implements Runnable {
    private final static Logger log = LoggerFactory.getLogger(NumberPrinter.class);
    private final static int printIntervalMillis = 1000;
    private static int currentNumber = 1;

    private final Object monitor;

    private final int number;
    private final String name;

    private boolean increase = true;

    public NumberPrinter(int number, Object monitor) {
        this.number = number;
        this.name = String.format("Поток #%d: ", number);
        this.monitor = monitor;
    }

    public void run() {
        try {
            synchronized (monitor) {
                while (currentNumber != number) {
                    monitor.wait();
                }
                currentNumber += 1;
                monitor.notifyAll();
            }

            log.info("Поток #{} начал работу", number);
            while (!Thread.currentThread().isInterrupted()) {
                printNumbers();
            }

        } catch (InterruptedException e) {
            log.info("Поток #{} прерван", number);
        }
    }

    private void printNumbers() throws InterruptedException {
        if (increase) {
            for (int j = 1; j <= 10; j++) {
                System.out.println(name + j);
                Utils.sleep(printIntervalMillis);
            }
        } else {
            for (int j = 9; j > 1; j--) {
                System.out.println(name + j);
                Utils.sleep(printIntervalMillis);
            }
        }
        increase = !increase;
    }
}
