package ru.otus.cachehw;

public enum Action {
    ADD("Add to cache"),
    GET("Get from cache"),
    REMOVE("Remove from cache");

    private final String name;

    Action(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
