package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.annotations.Log;


public class TestLogging implements TestLoggingInterface {
    private final static Logger logger = LoggerFactory.getLogger(TestLogging.class);

    @Log
    @Override
    public void calculation() {
        logger.info("Method without parameters");
    }

    @Log
    @Override
    public void calculation(int param) {
        logger.info("Method with one parameters");
    }

    @Log
    @Override
    public void calculation(int param1, int param2) {
        logger.info("Method with two parameters");
    }

    @Log
    @Override
    public void calculation(int param1, int param2, String param3) {
        logger.info("Method with three parameters");
    }

    @Log
    @Override
    public int calculation(int param1, int param2, int param3) {
        logger.info("Method with return value");
        return param1 + param2 + param3;
    }
}
