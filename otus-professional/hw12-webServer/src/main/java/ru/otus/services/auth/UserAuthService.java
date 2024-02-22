package ru.otus.services.auth;

public interface UserAuthService {
    boolean authenticate(String login, String password);
}
