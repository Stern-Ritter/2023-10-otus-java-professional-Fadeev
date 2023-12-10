package ru.otus.utils;

public class TestStatistic {
    private static final String TEST_RESULT_DELIMITER = "-".repeat(12);
    private static final String TESTS_RESULT_TEMPLATE = """
            Запущено тестов: %d.
            Успешно пройдено тестов: %d.
            Не пройдено тестов: %d.
            """;

    private int totalCount;
    private int passedCount;
    private int failedCount;

    public void testPassed() {
        totalCount += 1;
        passedCount += 1;
    }

    public void testFailed() {
        totalCount += 1;
        failedCount += 1;
    }

    public void printStatistic() {
        System.out.println(TEST_RESULT_DELIMITER);
        System.out.printf(TESTS_RESULT_TEMPLATE, totalCount, passedCount, failedCount);
        System.out.println(TEST_RESULT_DELIMITER);
    }
}
