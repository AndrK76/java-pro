package ru.otus.api.model;


import java.time.LocalDateTime;

public class SensorData {
    private final LocalDateTime measurementTime;
    private final String room;
    private final Double value;

    public SensorData(LocalDateTime measurementTime, String room, Double value) {
        this.measurementTime = measurementTime;
        this.room = room;
        this.value = value;
    }

    public LocalDateTime getMeasurementTime() {
        return measurementTime;
    }

    public String getRoom() {
        return room;
    }

    public Double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "SensorData{" +
                "measurementTime=" + measurementTime +
                ", room='" + room + '\'' +
                ", value=" + value +
                '}';
    }

    // Решил считать эквивалентными измерения из одной комнаты в одно время, значения не учитываю, ТЗ не противоречит :-)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SensorData that = (SensorData) o;

        if (getMeasurementTime() != null ? !getMeasurementTime().equals(that.getMeasurementTime()) : that.getMeasurementTime() != null)
            return false;
        return getRoom() != null ? getRoom().equals(that.getRoom()) : that.getRoom() == null;
    }

    @Override
    public int hashCode() {
        int result = getMeasurementTime() != null ? getMeasurementTime().hashCode() : 0;
        result = 31 * result + (getRoom() != null ? getRoom().hashCode() : 0);
        return result;
    }
}
