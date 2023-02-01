package ru.otus.andrk.tester;

import java.util.*;

public class RunTestStatistics {

    public static RunTestStatistics create(String testsName) {
        return new RunTestStatistics(testsName);
    }

    public static RunTestStatistics createWithFailedResult(String testsName, RunOneTestStatistic result) {
        var ret = new RunTestStatistics(testsName);
        RunOneTestStatistic retRes;
        //Можно передать успешный result, будет противоречие
        if (!result.isSuccess()) {
            retRes = result;
        } else {
            retRes = RunOneTestStatistic.createFailure(result);
        }

        ret.results.addAll(List.of(new RunOneTestStatistic[]{retRes}));
        return ret;
    }
    public static RunTestStatisticsFormatter getDefaultFormatter() {
        return new RunTestStatistics.DefaultFormatter();
    }


    public String getTestsName() {
        return testsName;
    }

    //TODO: комментарий
    public int getTestsSuccess() {
        return getCountByResType(RetResult.SUCCESS);
    }

    public int getTestsFailure() {
        return getCountByResType(RetResult.FAILURE);
    }

    //TODO: комментарий
    public Collection<RunOneTestStatistic> getResults() {
        return Collections.unmodifiableCollection(results);
    }

    public void addResult(RunOneTestStatistic testRes) {
        results.add(testRes);
    }

    public String prettyPrint(RunTestStatisticsFormatter formatter, RunOneTestStatisticsFormatter rowFormatter){
        return formatter.prettyPrint(this,rowFormatter);
    }
    public String prettyPrint(RunTestStatisticsFormatter formatter){
        return formatter.prettyPrint(this);
    }
    public String prettyPrint(RunOneTestStatisticsFormatter rowFormatter){
        RunTestStatisticsFormatter formatter = RunTestStatistics.getDefaultFormatter();
        return formatter.prettyPrint(this, rowFormatter);
    }
    public String prettyPrint(){
        RunTestStatisticsFormatter formatter = RunTestStatistics.getDefaultFormatter();
        return formatter.prettyPrint(this);
    }

    @Override
    public String toString() {
        return "RunTestStatistics{" +
                "testsName='" + testsName + '\'' +
                ", results=" + results +
                '}';
    }

    private static class DefaultFormatter implements RunTestStatisticsFormatter {
    }

    private RunTestStatistics(String testsName) {
        this.testsName = testsName;
        //TODO: оставить комментарий
        results = new ArrayList<>();
    }


    private final Collection<RunOneTestStatistic> results;

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
