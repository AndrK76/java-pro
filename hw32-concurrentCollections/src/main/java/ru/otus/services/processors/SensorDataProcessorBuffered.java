package ru.otus.services.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.api.SensorDataProcessor;
import ru.otus.api.model.SensorData;
import ru.otus.lib.SensorDataBufferedWriter;
import ru.otus.lib.SensorDataComparatorByMeasurementTime;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;

// Этот класс нужно реализовать
public class SensorDataProcessorBuffered implements SensorDataProcessor {
    private static final Logger log = LoggerFactory.getLogger(SensorDataProcessorBuffered.class);

    private final int bufferSize;
    private final SensorDataBufferedWriter writer;

    private final PriorityBlockingQueue<SensorData> bufferedData;

    public SensorDataProcessorBuffered(int bufferSize, SensorDataBufferedWriter writer) {
        this.bufferSize = bufferSize;
        this.writer = writer;
        this.bufferedData = new PriorityBlockingQueue<>(bufferSize, new SensorDataComparatorByMeasurementTime());
    }

    @Override
    public void process(SensorData data) {
        //log.info("process data {}", data);
        if (bufferedData.size() >= bufferSize) {
            flush();
        }
        if (!bufferedData.contains(data)) { //TODO: Вот здесь смущает возможно стоит на 1 блокировке обе операции делать?
            bufferedData.put(data);
        }
    }

    public void flush() {
        try {
            List<SensorData> content = new ArrayList<>();
            bufferedData.drainTo(content);
            if (content.size() > 0) {
                writer.writeBufferedData(content);

                //TODO: при включенном логе периодически тест shouldCorrectFlushDataFromManyThreads
                // "падает" по verify(processor, times(numberOfThreads)).flush();
                // org.mockito.exceptions.verification.TooFewActualInvocations:
                // sensorDataProcessorBuffered.flush();
                // Wanted 10 times:
                // -> at otus.services.processors.SensorDataProcessorBufferedTest.shouldCorrectFlushDataFromManyThreads(SensorDataProcessorBufferedTest.java:120)
                // But was 8 times:
                // ощущение что mock не успевает посчитать, что вызвал тест, подскажите так ли это?
                // Аналогично на shouldCorrectFlushDataAndWriteThreads assertThat(writer.getData()).hasSize(sensorDataList.size());
                // java.lang.AssertionError:
                // Expected size: 1999 but was: 994 in:
                // Спецэффект "ловится" в момент когда процессор ноута "подогревается" судя по вентилятору
                // Core i5-6200U  - 2 ядра с гипертрейдингом (4 логических процессора всего, 10 потоков многовато видать)
                //
                //log.info("flush to buffer");
            }
        } catch (Exception e) {
            log.error("Ошибка в процессе записи буфера", e);
        }
    }

    @Override
    public void onProcessingEnd() {
        flush();
    }
}
