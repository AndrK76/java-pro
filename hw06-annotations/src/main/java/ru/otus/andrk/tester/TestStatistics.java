package ru.otus.andrk.tester;

import java.util.Collection;

public interface TestStatistics {
    String getTestsName();

    int getTestsSuccess();

    int getTestsFailure();

    Collection<? extends OneTestStatistic> getResults();

    void addResult(OneTestStatistic testRes);

    @SuppressWarnings("unused")
    String prettyPrint(TestStatisticsFormatter formatter, OneTestStatisticsFormatter rowFormatter);
    @SuppressWarnings("unused")
    String prettyPrint(TestStatisticsFormatter formatter);
    @SuppressWarnings("unused")
    String prettyPrint(OneTestStatisticsFormatter rowFormatter);
    @SuppressWarnings("unused")
    String prettyPrint();

}
