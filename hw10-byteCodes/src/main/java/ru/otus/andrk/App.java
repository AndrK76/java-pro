package ru.otus.andrk;

import ru.otus.andrk.proxy.LoggingProxy;
import ru.otus.andrk.testlogging.LoggerImpl;
import ru.otus.andrk.testlogging.TestLoggingImpl;

import java.util.Arrays;

public class App {
    public static void main(String[] args) {
        var logger = new LoggerImpl();
        var logged = LoggingProxy.create(TestLoggingImpl.class, logger);
        System.out.println("Program output: ");
        logged.computeZero();
        logged.computeOne(1);
        logged.computeAnother("QQQQ");
        logged.computeTwo(1, 2);
        logged.computeThree(1, "Qwerty", logged);
        logged.computeAny("Many", 1, 2, 12.3, "строка", logged, logger, new TestLoggingImpl());
        logged.computeList(1,2,3,4,5,6,7,8);
        logged.computeList(Arrays.asList(1,2,3));

        System.out.println();
        System.out.println("Log:");
        System.out.println(logger.show());

    }
}
