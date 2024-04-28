package ru.otus.server;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.SequenceServiceGrpc;
import ru.otus.SequenceServiceOuterClass;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class SequenceServiceImpl extends SequenceServiceGrpc.SequenceServiceImplBase {
    private static final Logger logger = LoggerFactory.getLogger(SequenceServiceImpl.class);
    private static final long START_DELAY = 0;
    private static final long EXECUTE_INTERVAL = 2;

    @Override
    public void getSequence(SequenceServiceOuterClass.SequenceMessage request,
                            StreamObserver<SequenceServiceOuterClass.ElementMessage> responseObserver) {
        logger.info("Получен запрос на последовательность чисел (начальное значение:{}, конечное значение: {}, шаг: {})",
                request.getStart(), request.getEnd(), request.getStep());

        AtomicLong currentValue = new AtomicLong(request.getStart());
        var scheduler = Executors.newSingleThreadScheduledExecutor();

        Runnable sendNextSequenceValue = () -> {
            var elementMessage = SequenceServiceOuterClass.ElementMessage
                    .newBuilder()
                    .setCurrent(currentValue.longValue())
                    .build();
            responseObserver.onNext(elementMessage);

            currentValue.addAndGet(request.getStep());

            if (currentValue.longValue() == request.getEnd()) {
                scheduler.shutdown();
                responseObserver.onCompleted();
                logger.info("Все элементы последовательности чисел (начальное значение:{}, конечное значение: {}, шаг: {}) переданы",
                        request.getStart(), request.getEnd(), request.getStep());
            }
        };

        scheduler.scheduleAtFixedRate(sendNextSequenceValue, START_DELAY, EXECUTE_INTERVAL, TimeUnit.SECONDS);
    }
}
