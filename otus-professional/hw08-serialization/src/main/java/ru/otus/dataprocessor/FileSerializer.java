package ru.otus.dataprocessor;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

public class FileSerializer implements Serializer {
    private final File file;
    private final ObjectMapper mapper;

    public FileSerializer(String fileName) {
        file = new File(fileName);
        mapper = JsonMapper.builder().build();
    }

    @Override
    public void serialize(Map<String, Double> data) {
        try {
            mapper.writeValue(file, data);
        } catch (IOException e) {
            throw new FileProcessException(e);
        }
    }
}
