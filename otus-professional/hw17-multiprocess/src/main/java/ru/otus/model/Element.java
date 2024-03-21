package ru.otus.model;

public class Element {
    private long value;

    public Element(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }
}
