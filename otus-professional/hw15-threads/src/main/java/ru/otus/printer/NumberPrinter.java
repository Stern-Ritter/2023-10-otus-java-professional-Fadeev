package ru.otus.printer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.sequence.Sequence;
import ru.otus.utils.Utils;

public class NumberPrinter implements Runnable {
    private final static Logger log = LoggerFactory.getLogger(NumberPrinter.class);

    private static int currentWorkingThreadNumber = 1;

    private final int numberOfThreads;
    private final int printIntervalMillis;
    private final Object monitor;


    private final int threadNumber;
    private final String name;
    private final Sequence<Integer> sequence;


    public NumberPrinter(int numberOfThreads, int printIntervalMillis, Object monitor, int threadNumber, Sequence<Integer> sequence) {
        this.numberOfThreads = numberOfThreads;
        this.printIntervalMillis = printIntervalMillis;
        this.monitor = monitor;
        this.threadNumber = threadNumber;
        this.name = String.format("Поток #%d: ", threadNumber);
        this.sequence = sequence;
    }

    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                synchronized (monitor) {
                    while (threadNumber != currentWorkingThreadNumber) {
                        monitor.wait();
                    }

                    print();
                    Utils.sleep(printIntervalMillis);

                    currentWorkingThreadNumber = nextThreadNumber();
                    monitor.notifyAll();
                }
            }
        } catch (InterruptedException e) {
            log.info("Поток #{} прерван", threadNumber);
        }
    }

    private void print() {
        System.out.printf("%s: %d\n", name, sequence.get());
    }

    private int nextThreadNumber() {
        return currentWorkingThreadNumber % numberOfThreads + 1;
    }
}
