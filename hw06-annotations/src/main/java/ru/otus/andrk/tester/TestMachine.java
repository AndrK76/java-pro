package ru.otus.andrk.tester;

import java.time.LocalDateTime;
import java.util.*;

public class TestMachine {
    public static Collection<RunTestStatistic> Run(String testClass)
    {
        try {
            return Run(Class.forName(testClass));
        }
        catch (ClassNotFoundException e)
        {
            var startErr =  RunTestStatistic.failure(testClass, LocalDateTime.now(),LocalDateTime.now(),e);
            return List.of(new RunTestStatistic[]{startErr});
        }
    }

    public static Collection<RunTestStatistic> Run(Class<?> testClass)
    {
        var test = new TestMachine(testClass);
        return  new LinkedList<>();
    }

    private Class<?> testClass;
    private LocalDateTime startTime;
    private TestMachine(Class<?> testClass) {
        this.testClass = testClass;
        this.startTime = LocalDateTime.now();
    }
}
