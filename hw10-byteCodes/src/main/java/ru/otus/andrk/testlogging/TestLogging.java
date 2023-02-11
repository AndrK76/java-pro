package ru.otus.andrk.testlogging;

import java.util.List;

public interface TestLogging {

    void computeZero();

    void computeOne(int arg1);

    void computeAnother(String arg1);

    void computeTwo(int arg1, int arg2);

    void computeThree(int arg1, String arg2, TestLogging arg3);

    void computeAny(String par2, Object... args);

    void computeList(List<Integer> args);

    void computeList(int... args);


}
