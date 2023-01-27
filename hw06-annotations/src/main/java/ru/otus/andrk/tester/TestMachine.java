package ru.otus.andrk.tester;

import java.time.LocalDateTime;


public class TestMachine {
    public static RunTestStatistics Run(String testClass) {
        try {
            return Run(Class.forName(testClass));
        } catch (ClassNotFoundException e) {
            var startErr = RunOneTestStatistic.failure(testClass, LocalDateTime.now(), LocalDateTime.now(),
                    new RuntimeException(String.format("Ошибка при поиске класса %s", testClass), e));
            return RunTestStatistics.fail(startErr);
        }
    }

    public static RunTestStatistics Run(Class<?> testClass) {
        var results = RunTestStatistics.create();
        var test = new TestMachine(testClass, results);
        return results;
    }

    private final RunTestStatistics results;
    private final Class<?> testClass;

    private final LocalDateTime startTime;

    private TestMachine(Class<?> testClass, RunTestStatistics results) {
        this.testClass = testClass;
        this.startTime = LocalDateTime.now();
        this.results = results;
    }
}
