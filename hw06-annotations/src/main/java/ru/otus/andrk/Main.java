package ru.otus.andrk;

import ru.otus.andrk.tester.TestMachine;
import ru.otus.andrk.testsamples.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("***Запуск теста для класса SampleTest***");
        SampleTest.runLog.clear();
        var testResult = TestMachine.run("ru.otus.andrk.testsamples.SampleTest");
        System.out.println(testResult.prettyPrint());
        System.out.println();
        System.out.println("Log:");
        for (var log : SampleTest.runLog) {
            System.out.printf("\t%s%n", log);
        }

    }
}