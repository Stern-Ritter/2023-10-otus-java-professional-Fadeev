package ru.otus;

import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.model.Address;
import ru.otus.model.Client;
import ru.otus.model.Phone;
import ru.otus.repository.datatemplate.DataTemplate;
import ru.otus.repository.datatemplate.DataTemplateHibernate;
import ru.otus.repository.user.InMemoryUserDao;
import ru.otus.repository.user.UserDao;
import ru.otus.server.ClientsWebServer;
import ru.otus.server.ClientsWebServerWithFilterBasedSecurity;
import ru.otus.services.auth.UserAuthService;
import ru.otus.services.auth.UserAuthServiceImpl;
import ru.otus.services.client.ClientService;
import ru.otus.services.client.DBClientService;
import ru.otus.services.template.TemplateProcessor;
import ru.otus.services.template.TemplateProcessorImpl;
import ru.otus.migrations.MigrationsExecutorFlyway;
import ru.otus.sessionmanager.TransactionManagerHibernate;
import ru.otus.util.HibernateUtils;
import org.hibernate.SessionFactory;

/*
    // Стартовая страница
    http://localhost:8080
    // Страница списка клиентов
    http://localhost:8080/clients
    // Страница создания нового клиента
    http://localhost:8080/create-client-form
*/

public class WebServerWithFilterBasedSecurityDemo {
    private static final Logger log = LoggerFactory.getLogger(WebServerWithFilterBasedSecurityDemo.class);

    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";
    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) {
        Configuration configuration = new Configuration().configure(HIBERNATE_CFG_FILE);
        executeMigration(configuration);
        ClientsWebServer clientsWebServer = getClientsWebServer(configuration);
        try {
            clientsWebServer.start();
            clientsWebServer.join();
        } catch (Exception ex) {
            log.error("Server run exception: {}", ex.getMessage());
        }
    }

    private static void executeMigration(Configuration configuration) {
        String dbUrl = configuration.getProperty("hibernate.connection.url");
        String dbUserName = configuration.getProperty("hibernate.connection.username");
        String dbPassword = configuration.getProperty("hibernate.connection.password");
        MigrationsExecutorFlyway migrationsExecutor = new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword);

        migrationsExecutor.executeMigrations();
    }

    private static ClientsWebServer getClientsWebServer(Configuration configuration) {
        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory(configuration, Address.class, Client.class, Phone.class);
        TransactionManagerHibernate transactionManager = new TransactionManagerHibernate(sessionFactory);

        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);

        DataTemplate<Client> clientTemplate = new DataTemplateHibernate<>(Client.class);
        ClientService clientService = new DBClientService(transactionManager, clientTemplate);

        UserDao userDao = new InMemoryUserDao();
        UserAuthService authService = new UserAuthServiceImpl(userDao);

        return new ClientsWebServerWithFilterBasedSecurity(WEB_SERVER_PORT, templateProcessor, authService, clientService);
    }
}
