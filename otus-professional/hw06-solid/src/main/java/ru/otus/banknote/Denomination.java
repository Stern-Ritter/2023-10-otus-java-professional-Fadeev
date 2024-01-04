package ru.otus.banknote;

public enum Denomination {
    ONE_HUNDRED(100),
    TWO_HUNDRED(200),
    FIVE_HUNDRED(500),
    ONE_THOUSAND(1000),
    FIVE_THOUSAND(5000),
    TEN_THOUSAND(10000);

    private final int value;

    Denomination(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
