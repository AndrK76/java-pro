package ru.otus.andrk;

import ru.otus.andrk.tester.RunTestStatistic;
import ru.otus.andrk.tester.TestMachine;

import java.io.IOException;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {

        System.out.println(RunTestStatistic.success("kjjkljl - олролрол",
                LocalDateTime.now(),LocalDateTime.now().plusNanos(54654645)));
        System.out.println(RunTestStatistic.failure("kjjkljl - олролрол",
                LocalDateTime.now(),LocalDateTime.now().plusNanos(54654645),
                new RuntimeException("внешняя ошибка", new IOException("внутрянняя"))));
        var tst1 = TestMachine.Run("ru.otus.andrk.tests.BigTest");
        tst1 = TestMachine.Run(ru.otus.andrk.tests.BigTest.class);
    }
}