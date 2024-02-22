package ru.otus.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class User {
    private final Long id;
    private final String name;
    private final String login;
    private final String password;
}
