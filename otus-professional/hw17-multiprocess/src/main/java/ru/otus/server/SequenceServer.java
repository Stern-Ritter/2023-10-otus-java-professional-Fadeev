package ru.otus.server;

import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class SequenceServer {
    private static final Logger logger = LoggerFactory.getLogger(SequenceServer.class);
    private static final int SERVER_PORT = 8083;

    public void run() throws InterruptedException {
        var server = ServerBuilder.forPort(SERVER_PORT)
                .addService(new SequenceServiceImpl())
                .build();

        try {
            server.start();
            logger.info("Сервер запущен на порту: {}.", SERVER_PORT);
            server.awaitTermination();
        } catch (IOException e) {
            logger.error("Ошибка запуска сервера: {}.", e.getMessage());
        } catch (InterruptedException e) {
            logger.error("Работа сервера прервана.");
            throw new InterruptedException();
        }
    }
}
