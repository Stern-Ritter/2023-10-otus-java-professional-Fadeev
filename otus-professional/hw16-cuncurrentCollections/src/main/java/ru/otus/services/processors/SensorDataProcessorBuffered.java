package ru.otus.services.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.api.SensorDataProcessor;
import ru.otus.api.model.SensorData;
import ru.otus.lib.SensorDataBufferedWriter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@SuppressWarnings({"java:S1068", "java:S125"})
public class SensorDataProcessorBuffered implements SensorDataProcessor {
    private static final Logger log = LoggerFactory.getLogger(SensorDataProcessorBuffered.class);

    private final int bufferSize;
    private final SensorDataBufferedWriter writer;
    private final BlockingQueue<SensorData> dataBuffer;

    public SensorDataProcessorBuffered(int bufferSize, SensorDataBufferedWriter writer) {
        this.bufferSize = bufferSize;
        this.writer = writer;
        this.dataBuffer = new ArrayBlockingQueue<>(bufferSize);
    }

    @Override
    public void process(SensorData data) {
        checkBufferSize();

        boolean isAdded = dataBuffer.offer(data);
        if (!isAdded) {
            log.error("Ошибка обработки данных сенсора, буфер переполнен");
        }
    }

    public void flush() {
        try {
            List<SensorData> data = new ArrayList<>();
            if (dataBuffer.drainTo(data, bufferSize) == 0) {
                return;
            }
            List<SensorData> sortedData = sortData(data, Comparator.comparing(SensorData::getMeasurementTime));
            writer.writeBufferedData(sortedData);
        } catch (Exception e) {
            log.error("Ошибка в процессе записи буфера", e);
        }
    }

    @Override
    public void onProcessingEnd() {
        flush();
    }

    private void checkBufferSize() {
        if (dataBuffer.size() >= bufferSize) {
            flush();
        }
    }

    private List<SensorData> sortData(List<SensorData> data, Comparator<SensorData> comparator) {
        return data.stream()
                .sorted(comparator)
                .toList();
    }
}
