package ru.otus.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.HwCache;
import ru.otus.cachehw.MyCache;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.sessionmanager.TransactionRunner;
import ru.otus.crm.model.Manager;

import java.util.List;
import java.util.Optional;

public class DbServiceManagerImpl implements DBServiceManager {
    private static final String CACHE_TEMPLATE = "Manager #%d";
    private static final Logger log = LoggerFactory.getLogger(DbServiceManagerImpl.class);

    private final DataTemplate<Manager> managerDataTemplate;
    private final TransactionRunner transactionRunner;
    private final HwCache<String, Manager> cache;

    public DbServiceManagerImpl(TransactionRunner transactionRunner, DataTemplate<Manager> managerDataTemplate) {
        this.transactionRunner = transactionRunner;
        this.managerDataTemplate = managerDataTemplate;
        this.cache = new MyCache<>();
        this.cache.addListener((key, value, action) -> log.info("key:{}, value:{}, action: {}", key, value, action));
    }

    @Override
    public Manager saveManager(Manager manager) {
        return transactionRunner.doInTransaction(connection -> {
            if (manager.getNo() == null) {
                var managerNo = managerDataTemplate.insert(connection, manager);
                var createdManager = new Manager(managerNo, manager.getLabel(), manager.getParam1());
                cache.put(getCacheKey(managerNo), createdManager);
                log.info("created manager: {}", createdManager);
                return createdManager;
            }
            managerDataTemplate.update(connection, manager);
            cache.put(getCacheKey(manager.getNo()), manager);
            log.info("updated manager: {}", manager);
            return manager;
        });
    }

    @Override
    public Optional<Manager> getManager(long no) {
        return transactionRunner.doInTransaction(connection -> {
            Manager cachedManager = cache.get(getCacheKey(no));

            if (cachedManager != null) {
                log.info("manager: {}", Optional.of(cachedManager));
                return Optional.of(cachedManager);
            }

            var managerOptional = managerDataTemplate.findById(connection, no);
            if (managerOptional.isPresent()) {
                var savedManager = managerOptional.get();
                cache.put(getCacheKey(savedManager.getNo()), savedManager);
            }

            log.info("manager: {}", managerOptional);
            return managerOptional;
        });
    }

    @Override
    public List<Manager> findAll() {
        return transactionRunner.doInTransaction(connection -> {
            var managerList = managerDataTemplate.findAll(connection);
            log.info("managerList:{}", managerList);
            return managerList;
        });
    }

    private String getCacheKey(Long id) {
        return String.format(CACHE_TEMPLATE, id);
    }
}
