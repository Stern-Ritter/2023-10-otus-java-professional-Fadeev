package ru.otus.repository.user;

import java.util.Optional;
import ru.otus.model.User;

public interface UserDao {

    Optional<User> findById(long id);

    Optional<User> findByLogin(String login);
}
