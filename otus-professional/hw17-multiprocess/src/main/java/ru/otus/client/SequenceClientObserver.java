package ru.otus.client;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.SequenceServiceOuterClass;


public class SequenceClientObserver implements StreamObserver<SequenceServiceOuterClass.ElementMessage> {
    private static final Logger logger = LoggerFactory.getLogger(SequenceClientObserver.class);

    private long currentValue;

    public SequenceClientObserver() {
        this.currentValue = 0;
    }

    @Override
    public void onNext(SequenceServiceOuterClass.ElementMessage element) {
        increaseCurrentValue(element.getCurrent());
        logger.info("Текущее значение последовательности: {}", element.getCurrent());
    }

    @Override
    public void onError(Throwable t) {
        logger.error("Ошибка получения элемента последовательности: {}", t.getMessage());
    }

    @Override
    public void onCompleted() {
        logger.info("Все элементы последовательности получены");
    }


    public synchronized long increaseCurrentValue(long increment) {
        currentValue += increment;
        return currentValue;
    }
}
