package ru.otus.lib;

import ru.otus.api.model.SensorData;

import java.util.Comparator;

// Вынес в отдельный компаратор, а не добавил Comparable для SensorData исходя из того, что где-то может потребоваться
// сравнение по другим условиям. Для ДЗ можно было и в SensorData Comparable реализовать
public class SensorDataComparatorByMeasurementTime implements Comparator<SensorData> {
    @Override
    public int compare(SensorData o1, SensorData o2) {
        return o1.getMeasurementTime().compareTo(o2.getMeasurementTime());
    }
}
