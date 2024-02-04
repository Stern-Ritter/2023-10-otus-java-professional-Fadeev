package ru.otus.dataprocessor;

import ru.otus.model.Measurement;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ProcessorAggregator implements Processor {

    @Override
    public Map<String, Double> process(List<Measurement> data) {
        return data.stream().reduce(new TreeMap<>(), (map, e) -> {
            map.merge(e.name(), e.value(), Double::sum);
            return map;
        }, (m, m2) -> {
            m.putAll(m2);
            return m;
        });
    }
}
