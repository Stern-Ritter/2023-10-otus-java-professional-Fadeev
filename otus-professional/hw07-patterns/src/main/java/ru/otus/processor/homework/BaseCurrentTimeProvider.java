package ru.otus.processor.homework;

import java.util.concurrent.TimeUnit;

public class BaseCurrentTimeProvider implements CurrentTimeProvider {
    @Override
    public long getCurrentTimeSeconds() {
        return TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
    }
}
