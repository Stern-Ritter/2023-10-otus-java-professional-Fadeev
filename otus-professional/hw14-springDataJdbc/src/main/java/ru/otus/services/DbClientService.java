package ru.otus.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.model.Client;
import ru.otus.repository.ClientRepository;
import ru.otus.sessionmanager.TransactionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DbClientService implements ClientService {
    private static final Logger log = LoggerFactory.getLogger(DbClientService.class);

    private final TransactionManager transactionManager;
    private final ClientRepository clientRepository;

    @Autowired
    public DbClientService(TransactionManager transactionManager, ClientRepository clientRepository) {
        this.transactionManager = transactionManager;
        this.clientRepository = clientRepository;
    }

    @Override
    public List<Client> findAll() {
        var clientList = new ArrayList<>(clientRepository.findAll());
        log.info("clientList:{}", clientList);
        return clientList;
    }

    @Override
    public Optional<Client> findById(Long id) {
        var clientOptional = clientRepository.findById(id);
        log.info("client: {}", clientOptional);
        return clientOptional;
    }

    @Override
    public Optional<Client> findByName(String name) {
        var clientOptional = clientRepository.findByName(name);
        log.info("client: {}", clientOptional);
        return clientOptional;
    }

    @Override
    public Client save(Client client) {
        return transactionManager.doInTransaction(() -> {
            var savedClient = clientRepository.save(client);
            log.info("saved client: {}", savedClient);
            return savedClient;
        });
    }
}
