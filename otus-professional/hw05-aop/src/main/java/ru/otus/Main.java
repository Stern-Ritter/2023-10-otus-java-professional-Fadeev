package ru.otus;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class Main {
    private final static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        TestLoggingInterface instance = DynamicProxy.createInstance(new TestLogging(), TestLoggingInterface.class);
        instance.calculation();
        instance.calculation(1);
        instance.calculation(1, 2);
        instance.calculation(1, 2);
        instance.calculation(1, 2, "3");
        instance.calculation(1, 2, "3");
        logger.info("First call method: calculation(int arg0, int arg1,  int arg2) - {}", instance.calculation(10, 20, 30));
        logger.info("Second call method: calculation(int arg0, int arg1,  int arg2) - {}", instance.calculation(10, 20, 30));
    }
}