package ru.otus.andrk.tester;

import java.time.LocalDateTime;

public interface OneTestStatistic {
    String getNameTest();

    LocalDateTime getStartTime();

    LocalDateTime getEndTime();

    boolean isSuccess();

    Throwable getError();

    @SuppressWarnings("unused")
    String prettyPrint(OneTestStatisticsFormatter formatter);

    @SuppressWarnings("unused")
    String prettyPrint();
}
