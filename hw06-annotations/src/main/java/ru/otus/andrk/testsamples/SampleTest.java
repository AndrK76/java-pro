package ru.otus.andrk.testsamples;

import ru.otus.andrk.annotations.After;
import ru.otus.andrk.annotations.Before;
import ru.otus.andrk.annotations.Test;
import ru.otus.andrk.annotations.TestName;

import java.util.ArrayList;

@Test
public class SampleTest {
    public static ArrayList<String> runLog = new ArrayList<>();

    public SampleTest() {
        runLog.add(String.format("Создан экземпляр класса: " + this.toString()));
    }

    @Before
    private void initTest() {
        runLog.add(String.format("\tвызван метод initTest для экземпляра " + this.toString()));
    }

    @Before
    public void initTest2() {
        runLog.add(String.format("\tвызван метод initTest2 для экземпляра " + this.toString()));
    }

    @Test
    @TestName("Произвольный тест")
    public void doSameTest() throws Exception {
        runLog.add(String.format("*\tвызван метод doSameTest для экземпляра " + this.toString()));
        Thread.sleep(300L);
        throw new NullPointerException("test");
    }


    @Test
    public void doAnotherTest() throws InterruptedException {
        runLog.add(String.format("*\tвызван метод doAnotherTest для экземпляра " + this.toString()));
        Thread.sleep(550L);
    }

    @After
    void finishTest() {
        runLog.add(String.format("\tвызван метод finishTest для экземпляра " + this.toString()));
    }

}
