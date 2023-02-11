package ru.otus.andrk.testlogging;


import java.util.List;

public class TestLoggingImpl implements TestLogging {
    @Log
    @Override
    public void computeZero() {
        System.out.println("computeZero()");
    }

    @Log
    @Override
    public void computeOne(int arg1) {
        System.out.println("computeOne()");
    }

    @Override
    public void computeAnother(String arg1) {
        System.out.println("computeAnother()");
    }

    @Log
    @Override
    public void computeTwo(int arg1, int arg2) {
        System.out.println("computeTwo()");
    }

    @Log
    @Override
    public void computeThree(int arg1, String arg2, TestLogging arg3) {
        System.out.println("computeThree()");
    }

    @Log
    @Override
    public void computeAny(String par2, Object... args) {
        System.out.println("computeAny()");
    }

    @Override
    @Log
    public void computeList(List<Integer> args) {
        System.out.println("computeList(list)");
    }

    @Override
    @Log
    public void computeList(int... args) {
        System.out.println("computeList([]])");
    }

    @Override
    @Log
    public String toString() {
        return "TestLoggingImpl{}";
    }
}
