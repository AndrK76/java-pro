package ru.otus.andrk.tester;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class RunTestStatistics {

    public static RunTestStatistics create(String testsName)
    {
        return new RunTestStatistics(testsName);
    }
    public static RunTestStatistics createWithFailedResult(String testsName,RunOneTestStatistic result)
    {
        var ret = new RunTestStatistics(testsName);
        ret.results.addAll(List.of(new RunOneTestStatistic[]{result}));
        ret.testsFailure = 1;
        return ret;
    }

    public String getTestsName() {
        return testsName;
    }

    public int getTestsSuccess() {
        return testsSuccess;
    }

    public int getTestsFailure() {
        return testsFailure;
    }

    public Collection<RunOneTestStatistic> getResults() {
        return results;
    }

    public void addResult(RunOneTestStatistic testRes){
        results.add(testRes);
        if (testRes.isSuccess()){
            testsSuccess++;
        }
        else {
            testsFailure++;
        }
    }


    private RunTestStatistics(String testsName) {
        this.testsName = testsName;
        results = new LinkedList<>();
    }

    private final Collection<RunOneTestStatistic> results;

    private final String testsName;

    private int testsSuccess = 0;
    private int testsFailure = 0;


    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder(results.size()+1);
        ret.append(String.format("Результат выполнения тестов %s: выполнено %d тестов, успешно %d, с ошибкой %d%n",
                testsName, testsSuccess+testsFailure,testsSuccess,testsFailure));
        for (var res : results) ret.append(String.format("%s%n",res.toString()));
        return ret.toString();
    }

}
