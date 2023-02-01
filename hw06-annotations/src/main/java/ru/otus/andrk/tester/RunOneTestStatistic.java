package ru.otus.andrk.tester;

import java.time.LocalDateTime;


public class RunOneTestStatistic implements OneTestStatistic{

    public static OneTestStatistic createSuccess(String nameTest, LocalDateTime startTime, LocalDateTime endTime) {
        return new RunOneTestStatistic(nameTest, startTime, endTime, true, null);
    }

    public static OneTestStatistic createSuccess(OneTestStatistic origResult) {
        return new RunOneTestStatistic(origResult.getNameTest(), origResult.getStartTime(), origResult.getEndTime(), true, null);
    }

    public static OneTestStatistic createFailure(String nameTest, LocalDateTime startTime, LocalDateTime endTime, Throwable error) {
        return new RunOneTestStatistic(nameTest, startTime, endTime, false, error);
    }

    public static OneTestStatistic createFailure(OneTestStatistic origResult) {
        return new RunOneTestStatistic(origResult.getNameTest(), origResult.getStartTime(),
                origResult.getEndTime(), false, origResult.getError());
    }

    public static OneTestStatisticsFormatter getDefaultFormatter() {
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

    public String prettyPrint(OneTestStatisticsFormatter formatter) {
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

    private static class DefaultFormatter implements OneTestStatisticsFormatter {
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
