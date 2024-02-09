package ru.otus.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.HwCache;
import ru.otus.cachehw.MyCache;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.sessionmanager.TransactionRunner;
import ru.otus.crm.model.Client;

import java.util.List;
import java.util.Optional;

public class DbServiceClientImpl implements DBServiceClient {
    private static final String CACHE_TEMPLATE = "Client #%d";
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);

    private final DataTemplate<Client> dataTemplate;
    private final TransactionRunner transactionRunner;
    private final HwCache<String, Client> cache;

    public DbServiceClientImpl(TransactionRunner transactionRunner, DataTemplate<Client> dataTemplate) {
        this.transactionRunner = transactionRunner;
        this.dataTemplate = dataTemplate;
        this.cache = new MyCache<>();
        this.cache.addListener((key, value, action) -> log.info("key:{}, value:{}, action: {}", key, value, action));
    }

    @Override
    public Client saveClient(Client client) {
        return transactionRunner.doInTransaction(connection -> {
            if (client.getId() == null) {
                var clientId = dataTemplate.insert(connection, client);
                var createdClient = new Client(clientId, client.getName());
                cache.put(getCacheKey(clientId), createdClient);
                log.info("created client: {}", createdClient);
                return createdClient;
            }
            dataTemplate.update(connection, client);
            cache.put(getCacheKey(client.getId()), client);
            log.info("updated client: {}", client);
            return client;
        });
    }

    @Override
    public Optional<Client> getClient(long id) {
        return transactionRunner.doInTransaction(connection -> {
            Client cachedClient = cache.get(getCacheKey(id));

            if (cachedClient != null) {
                log.info("client: {}", Optional.of(cachedClient));
                return Optional.of(cachedClient);
            }

            var clientOptional = dataTemplate.findById(connection, id);
            if (clientOptional.isPresent()) {
                var savedClient = clientOptional.get();
                cache.put(getCacheKey(savedClient.getId()), savedClient);
            }

            log.info("client: {}", clientOptional);
            return clientOptional;
        });
    }

    @Override
    public List<Client> findAll() {
        return transactionRunner.doInTransaction(connection -> {
            var clientList = dataTemplate.findAll(connection);
            log.info("clientList:{}", clientList);
            return clientList;
        });
    }

    private String getCacheKey(Long id) {
        return String.format(CACHE_TEMPLATE, id);
    }
}
