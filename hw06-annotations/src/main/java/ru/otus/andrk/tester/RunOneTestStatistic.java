package ru.otus.andrk.tester;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;


public class RunOneTestStatistic {

    public static RunOneTestStatistic createSuccess(String nameTest, LocalDateTime startTime, LocalDateTime endTime) {
        return new RunOneTestStatistic(nameTest, startTime, endTime, true, null);
    }

    public static RunOneTestStatistic createSuccess(RunOneTestStatistic origResult) {
        return new RunOneTestStatistic(origResult.nameTest, origResult.startTime, origResult.endTime, true, null);
    }

    public static RunOneTestStatistic createFailure(String nameTest, LocalDateTime startTime, LocalDateTime endTime, Throwable error) {
        return new RunOneTestStatistic(nameTest, startTime, endTime, false, error);
    }

    public static RunOneTestStatistic createFailure(RunOneTestStatistic origResult) {
        return new RunOneTestStatistic(origResult.nameTest, origResult.startTime, origResult.endTime, false, origResult.error);
    }

    public static RunOneTestStatisticsFormatter getDefaultFormatter() {
        return new DefaultFormatter();
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

    public boolean isSuccess() {
        return result;
    }

    public Throwable getError() {
        return error;
    }

    public String prettyPrint(RunOneTestStatisticsFormatter formatter) {
        return formatter.prettyPrint(this);
    }

    public String prettyPrint() {
        return RunOneTestStatistic.getDefaultFormatter().prettyPrint(this);
    }


    @Override
    public String toString() {
        return "RunOneTestStatistic{" +
                "nameTest='" + nameTest + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", result=" + result +
                ", error=" + error +
                '}';
    }

    private static class DefaultFormatter implements RunOneTestStatisticsFormatter {
    }

    private final String nameTest;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final boolean result;
    private final Throwable error;

    private RunOneTestStatistic(String nameTest, LocalDateTime startTime, LocalDateTime endTime, boolean result, Throwable error) {
        this.nameTest = nameTest;
        this.startTime = startTime;
        this.endTime = endTime;
        this.result = result;
        this.error = error;
    }

}
