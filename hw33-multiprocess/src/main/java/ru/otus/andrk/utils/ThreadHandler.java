package ru.otus.andrk.utils;

public class ThreadHandler {
    public static void sleep(int timeout) {
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
        }
    }
}
