package ru.otus.andrk.tester;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;


public class RunOneTestStatistic {

    public static RunOneTestStatistic success(String nameTest, LocalDateTime startTime, LocalDateTime endTime) {
        return new RunOneTestStatistic(nameTest, startTime, endTime, true, null);
    }

    public static RunOneTestStatistic failure(String nameTest, LocalDateTime startTime, LocalDateTime endTime, Throwable error) {
        return new RunOneTestStatistic(nameTest, startTime, endTime, false, error);
    }

    public String getNameTest() {
        return nameTest;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public boolean isResult() {
        return result;
    }

    public Throwable getError() {
        return error;
    }

    @Override
    public String toString() {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        StringBuilder sb = new StringBuilder(4);
        sb.append(String.format("Тест %s: %s", nameTest, result ? "Успех" : "Ошибка"));
        sb.append(String.format("%n\tНачало теста %s, продолжительность: %dмс"
                , getStartTime().format(dtf), ChronoUnit.MILLIS.between(endTime, startTime)));
        if (error != null) {
            sb.append(String.format("%n\tОшибка выполнения: %s", error.toString()));
            if (error.getCause() != null){
                sb.append(String.format(" (%s)", error.getCause().toString()));
            }
        }
        return sb.toString();
    }

    private String nameTest;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean result;
    private Throwable error;

    private RunOneTestStatistic(String nameTest, LocalDateTime startTime, LocalDateTime endTime, boolean result, Throwable error) {
        this.nameTest = nameTest;
        this.startTime = startTime;
        this.endTime = endTime;
        this.result = result;
        this.error = error;
    }

}
