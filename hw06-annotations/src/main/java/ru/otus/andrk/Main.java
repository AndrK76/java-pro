package ru.otus.andrk;

import ru.otus.andrk.tester.RunOneTestStatistic;
import ru.otus.andrk.tester.TestMachine;

import java.io.IOException;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {

        System.out.println("***Start \"ru.otus.andrk.tests.BigTest1\"***");
        var testRes = TestMachine.Run("ru.otus.andrk.tests.BigTest1");
        System.out.println(testRes);
        System.out.println();

        System.out.println("***Start class ru.otus.andrk.tests.BigTest.class***");
        testRes = TestMachine.Run(ru.otus.andrk.tests.BigTest.class);
        System.out.println(testRes);
        System.out.println();

    }
}