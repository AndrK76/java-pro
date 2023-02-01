package ru.otus.andrk.tester;

public interface RunTestStatisticsFormatter {
    default String prettyPrint(RunTestStatistics result, RunOneTestStatisticsFormatter rowFormatter){
        int testsSuccess = result.getTestsSuccess();
        int testsFailure = result.getTestsFailure();
        StringBuilder ret = new StringBuilder(result.getResults().size() + 1);
        ret.append(String.format("Результат выполнения тестов %s: выполнено %d тестов, успешно %d, с ошибкой %d%n",
                result.getTestsName(), testsSuccess + testsFailure, testsSuccess, testsFailure));
        for (var res : result.getResults()) ret.append(String.format("%s%n", res.prettyPrint(rowFormatter)));
        return ret.toString();
    }
    default String prettyPrint(RunTestStatistics result){
        var formatter = RunOneTestStatistic.getDefaultFormatter();
        return prettyPrint(result,formatter);
    }


}
