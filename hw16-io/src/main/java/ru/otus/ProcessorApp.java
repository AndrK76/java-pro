package ru.otus;

import ru.otus.dataprocessor.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Отладочное приложение
 */
public class ProcessorApp {

    private final String inputFileName = "inputData.json";
    private final String outputFileName = "outputData.json";

    public static void main(String[] args) {
        new ProcessorApp().load();
    }

    void load() {
        Loader loader = new ResourcesFileLoader(inputFileName);
        var data = loader.load();
        System.out.println("ReadResult:");
        data.forEach(r -> System.out.println("\t" + r));
        Processor processor = new ProcessorAggregator();
        var res = processor.process(data);
        Serializer serializer = new FileSerializer(outputFileName);
        serializer.serialize(res);
        showFileAndDelete();
    }

    void showFileAndDelete() {
        System.out.println("Result:");
        try (var rdr = new BufferedReader(new FileReader(outputFileName))) {
            System.out.println("\t" + rdr.readLine());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        new File(outputFileName).delete();
    }
}
