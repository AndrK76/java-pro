package ru.otus.dataprocessor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import ru.otus.model.Measurement;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ResourcesFileLoader implements Loader {

    private final String fileName;

    public ResourcesFileLoader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<Measurement> load() {
        Type listOfMeasurementClass = new TypeToken<ArrayList<Measurement>>() {
        }.getType();
        try (
                var reader = new JsonReader(
                        new InputStreamReader(
                                Objects.requireNonNull(
                                        Loader.class.getClassLoader().getResourceAsStream(fileName))
                        ))
        ) {
            return new Gson().fromJson(reader, listOfMeasurementClass);
        } catch (IOException | NullPointerException e) {
            throw new FileProcessException(e);
        }
    }
}
