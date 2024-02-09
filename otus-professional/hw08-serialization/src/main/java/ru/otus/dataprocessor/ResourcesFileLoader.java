package ru.otus.dataprocessor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import ru.otus.model.Measurement;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ResourcesFileLoader implements Loader {
    private final File file;
    private final ObjectMapper mapper;


    public ResourcesFileLoader(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        String path = URLDecoder.decode(classLoader.getResource(fileName).getPath(), StandardCharsets.UTF_8);
        file = new File(path);
        mapper = JsonMapper.builder().build();
    }

    @Override
    public List<Measurement> load() {
        try {
            return mapper.readValue(file, new TypeReference<>() {
            });
        } catch (IOException e) {
            throw new FileProcessException(e);
        }
    }
}
