package ru.otus;

import ru.otus.annotations.Log;

public interface TestLoggingInterface {
    void calculation();

    void calculation(int param);

    void calculation(int param1, int param2);

    void calculation(int param1, int param2, String param3);

    int calculation(int param1, int param2, int param3);
}
