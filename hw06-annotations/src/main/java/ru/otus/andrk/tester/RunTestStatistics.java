package ru.otus.andrk.tester;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class RunTestStatistics {

    public static RunTestStatistics create()
    {
        return new RunTestStatistics();
    }
    public static RunTestStatistics fail(RunOneTestStatistic result)
    {
        var ret = new RunTestStatistics();
        ret.results.addAll(List.of(new RunOneTestStatistic[]{result}));
        ret.testsFailure = 1;
        return ret;
    }

    public int getTestsSuccess() {
        return testsSuccess;
    }

    public int getTestsFailure() {
        return testsFailure;
    }

    private RunTestStatistics() {
        results = new LinkedList<RunOneTestStatistic>();
    }

    public RunTestStatistics(RunOneTestStatistic[] results) {
        this();
        this.results.addAll(List.of(results));
    }

    public Collection<RunOneTestStatistic> getResults() {
        return results;
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder(results.size()+1);
        ret.append(String.format("Результат выполнения: выполнено %d тестов, успешно %d, с ошибкой %d%n",
                testsSuccess+testsFailure,testsSuccess,testsFailure));
        for (var res : results) ret.append(String.format("%s%n",res.toString()));
        return ret.toString();
    }

    private Collection<RunOneTestStatistic> results;
    private int testsSuccess = 0;
    private int testsFailure = 0;
}
