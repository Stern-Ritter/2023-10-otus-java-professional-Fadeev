package ru.otus.client;

import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.SequenceServiceGrpc;
import ru.otus.SequenceServiceOuterClass;
import ru.otus.utils.Utils;


public class SequenceClient {
    private static final Logger logger = LoggerFactory.getLogger(SequenceClient.class);

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8083;
    private static final long SEQUENCE_START = 0;
    private static final long SEQUENCE_END = 30;
    private static final long SEQUENCE_STEP = 1;
    private static final long ITERATIONS_COUNT = 50;
    private static final long ITERATIONS_STEP = 1;
    private static final int ITERATION_INTERVAL_MILLIS = 1000;

    public void run() throws InterruptedException {
        var channel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();
        var service = SequenceServiceGrpc.newStub(channel);
        var clientObserver = new SequenceClientObserver();

        acceptSequenceElements(service, clientObserver);
        printSequenceElements(clientObserver);

        logger.info("Клиент завершил свою работу");
        channel.shutdown();
    }

    private void acceptSequenceElements(SequenceServiceGrpc.SequenceServiceStub service,
                                        SequenceClientObserver clientObserver) {
        var request = SequenceServiceOuterClass.SequenceMessage.newBuilder()
                .setStart(SEQUENCE_START)
                .setEnd(SEQUENCE_END)
                .setStep(SEQUENCE_STEP)
                .build();


        service.getSequence(request, clientObserver);
    }

    private void printSequenceElements(SequenceClientObserver clientObserver) throws InterruptedException {
        try {
            for (int i = 0; i < ITERATIONS_COUNT; i++) {
                long currentValue = clientObserver.increaseCurrentValue(ITERATIONS_STEP);
                logger.info("Текущее значение: {}", currentValue);
                Utils.sleep(ITERATION_INTERVAL_MILLIS);
            }
        } catch (InterruptedException e) {
            logger.error("Работа клиента прервана.");
            throw new InterruptedException();
        }
    }
}
