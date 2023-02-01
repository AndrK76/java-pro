package ru.otus.andrk.tester;

import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public interface RunOneTestStatisticsFormatter {
    default String prettyPrint(RunOneTestStatistic result)
    {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        StringBuilder sb = new StringBuilder(4);
        sb.append(String.format("Тест %s: %s", result.getNameTest(), result.isSuccess() ? "Успех" : "Ошибка"));
        sb.append(String.format("%n\tНачало теста %s, продолжительность: %dмс"
                , result.getStartTime().format(dtf), ChronoUnit.MILLIS.between(result.getStartTime(), result.getEndTime())));
        if (result.getError() != null) {
            sb.append(String.format("%n\tОшибка выполнения: %s->%s",
                    result.getError().getClass().getSimpleName(), result.getError().getMessage()));
            if (result.getError().getCause() != null) {
                sb.append(String.format(" (%s->%s)",
                        result.getError().getCause().getClass().getSimpleName(), result.getError().getCause().getMessage()));
            }
        }
        return sb.toString();
    }
}
