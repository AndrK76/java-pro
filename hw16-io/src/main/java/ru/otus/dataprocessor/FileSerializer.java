package ru.otus.dataprocessor;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.TreeMap;

public class FileSerializer implements Serializer {

    private final String fileName;

    public FileSerializer(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void serialize(Map<String, Double> data) {
        //формирует результирующий json и сохраняет его в файл
        Type mapResultClass = new TypeToken<Map<String, Double>>() {
        }.getType();

        try (var jsonWriter = new JsonWriter(new FileWriter(fileName))) {
            TreeMap<String, Double> sortedData = new TreeMap<>(data);
            new Gson().toJson(sortedData, mapResultClass, jsonWriter);
        } catch (IOException | JsonIOException e) {
            throw new FileProcessException(e);
        }
    }
}
