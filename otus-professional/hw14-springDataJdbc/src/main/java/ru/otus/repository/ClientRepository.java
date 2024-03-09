package ru.otus.repository;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;
import ru.otus.model.Client;

import java.util.Optional;

@Repository
public interface ClientRepository extends ListCrudRepository<Client, Long> {
    Optional<Client> findByName(String name);
}
