package ru.otus.andrk.tester;

import java.util.*;

public class RunTestStatistics implements TestStatistics {

    public static RunTestStatistics create(String testsName) {
        return new RunTestStatistics(testsName);
    }

    public static RunTestStatistics createWithResult(String testsName, OneTestStatistic result) {
        var ret = new RunTestStatistics(testsName);
        ret.results.add(result);
        return ret;
    }
    public String getTestsName() {
        return testsName;
    }

    public int getTestsSuccess() {
        return getCountByResType(RetResult.SUCCESS);
    }

    public int getTestsFailure() {
        return getCountByResType(RetResult.FAILURE);
    }

    public Collection<OneTestStatistic> getResults() {
        return Collections.unmodifiableCollection(results);
    }

    public void addResult(OneTestStatistic testRes) {
        results.add(testRes);
    }

    public String prettyPrint(TestStatisticsFormatter formatter, OneTestStatisticsFormatter rowFormatter) {
        return formatter.prettyPrint(this, rowFormatter);
    }

    public String prettyPrint(TestStatisticsFormatter formatter) {
        return formatter.prettyPrint(this);
    }

    public String prettyPrint(OneTestStatisticsFormatter rowFormatter) {
        TestStatisticsFormatter formatter = RunTestStatistics.getDefaultFormatter();
        return formatter.prettyPrint(this, rowFormatter);
    }

    public String prettyPrint() {
        TestStatisticsFormatter formatter = RunTestStatistics.getDefaultFormatter();
        return formatter.prettyPrint(this);
    }

    public static TestStatisticsFormatter getDefaultFormatter() {
        return new RunTestStatistics.DefaultFormatter();
    }

    @Override
    public String toString() {
        return "RunTestStatistics{" +
                "testsName='" + testsName + '\'' +
                ", results=" + results +
                '}';
    }

    private static class DefaultFormatter implements TestStatisticsFormatter {
    }

    private RunTestStatistics(String testsName) {
        this.testsName = testsName;
        results = new ArrayList<>();
    }


    private final Collection<OneTestStatistic> results;

    private final String testsName;

    private enum RetResult {
        SUCCESS,
        FAILURE
    }

    private int getCountByResType(RetResult resType) {
        if (results.size() == 0) {
            return 0;
        }
        boolean querySuccessRes = resType.equals(RetResult.SUCCESS);
        return (int) results.stream().filter(r -> r.isSuccess() == querySuccessRes).count();
    }


}
